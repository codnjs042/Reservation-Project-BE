package com.example.demo.global.infra.vworld;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AreaService {
    private final VWorldClient vWorldClient;

    public List<AreaResponse> getList(String cd){
        return vWorldClient.getArea(cd);
    }
}
