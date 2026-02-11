package com.carservice.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class RepairmentForm {
    private List<RepairmentDto> repairments;
}
