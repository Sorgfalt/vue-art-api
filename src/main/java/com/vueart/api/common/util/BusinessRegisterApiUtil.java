package com.vueart.api.common.util;

import com.vueart.api.core.enums.Code;
import com.vueart.api.core.enums.Code.BusinessRegisterType;
import com.vueart.api.core.exception.VueArtApiException;
import com.vueart.api.dto.request.business.BusinessRegisterDto;
import com.vueart.api.dto.request.business.BusinessRegisterListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class BusinessRegisterApiUtil {
    @Value("${business.register.service.api}")
    private String serviceApi;
    @Value("${business.register.service.key}")
    private String serviceKey;

    private static final String BUSINESS_STATUS_ENDPOINT = "/status?serviceKey=";

    public boolean getBusinessRegisterCheck(BusinessRegisterListDto req) {
        try {
            log.info("getBusinessRegisterCheck start");
            RestClient restClient = RestClient.builder()
                    .baseUrl(serviceApi)
                    .defaultHeaders(headers -> {
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
                    })
                    .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, res) -> {
                        throw new VueArtApiException(Code.ErrorCode.BAD_REQUEST);
                    })
                    .build();

            List<String> bNoList = req.getData().stream()
                    .map(BusinessRegisterDto::getB_no)
                    .toList();

            String encodedKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
            String uriStr = serviceApi + BUSINESS_STATUS_ENDPOINT + encodedKey;
            URI uri = URI.create(uriStr);

            ResponseEntity<Map<String, Object>> response = restClient.post()
                    .uri(uri)
                    .body(Map.of("b_no", bNoList))
                    .retrieve()
                    .onStatus(HttpStatusCode::is5xxServerError, (request, res) -> {
                        throw new VueArtApiException(Code.ErrorCode.INTERNAL_SERVER_ERROR);
                    })
                    .toEntity(new ParameterizedTypeReference<>() {
                    });

            Map<String, Object> body = response.getBody();
            if (body == null || !body.containsKey("data")) return false;

            var dataList = (List<Map<String, Object>>) body.get("data");
            if (dataList.isEmpty()) return false;

            String bSttCd = (String) dataList.get(0).get("b_stt_cd");
            BusinessRegisterType type = BusinessRegisterType.fromCode(bSttCd);
            return type == BusinessRegisterType.VALID;
        } catch (Exception e) {
            log.error("getBusinessRegisterCheck error", e);
            return false;
        }
    }
}
