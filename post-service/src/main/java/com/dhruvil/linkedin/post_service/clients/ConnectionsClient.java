package com.dhruvil.linkedin.post_service.clients;

import com.dhruvil.linkedin.post_service.dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "connections-service", path = "/connections")
public interface ConnectionsClient {

    @GetMapping("core/first-degree")
    List<PersonDto> getFirstConnections();

}