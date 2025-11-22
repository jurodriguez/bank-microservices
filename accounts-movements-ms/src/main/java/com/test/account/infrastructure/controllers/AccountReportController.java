package com.test.account.infrastructure.controllers;

import com.test.account.application.dto.AccountReportItem;
import com.test.account.application.usecases.account.AccountReportUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDate;


@RestController
@RequestMapping()
@RequiredArgsConstructor
public class AccountReportController {

    private final AccountReportUseCase accountReportUseCase;

    @GetMapping("/reportes")
    public Flux<AccountReportItem> report(
            @RequestParam("fecha_inicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("fecha_final") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam("cliente") Long customerId
    ) {
        return accountReportUseCase.execute(start, end, customerId);
    }

}
