package com.sumpositive.ticketing.dashboard.dto;

public record Dashboard(
    long total,
    long open,
    long inProgress,
    long resolved,
    long closed
) {}
