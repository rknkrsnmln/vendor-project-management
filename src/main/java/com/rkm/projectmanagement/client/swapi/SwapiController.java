package com.rkm.projectmanagement.client.swapi;

import com.rkm.projectmanagement.client.swapi.dto.SwapiResponseDto;
import com.rkm.projectmanagement.dtos.ResultBaseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.endpoint.base-url}/swapi")
public class SwapiController {

    private final SwapiService swapiService;

    public SwapiController(SwapiService swapiService) {
        this.swapiService = swapiService;
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<ResultBaseDto<SwapiResponseDto>> searchPeople(@PathVariable(name = "name") String name){
        SwapiResponseDto swapiResponseDto = swapiService.searchPeopleOnSwapi(name);
        return new ResponseEntity<>(ResultBaseDto.<SwapiResponseDto>builder()
                .code(HttpStatus.OK.value())
                .message("Success getting all search result")
                .data(swapiResponseDto)
                .build(), HttpStatus.OK);
    }

}
