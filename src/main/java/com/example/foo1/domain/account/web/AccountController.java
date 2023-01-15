package com.example.foo1.domain.account.web;

import com.example.foo1.domain.account.app.AccountApplicationService;
import com.example.foo1.domain.account.dto.AccountTransactionDto;
import com.example.foo1.domain.account.dto.GetAccountTransactionRequest;
import com.example.foo1.domain.account.web.request.TransferAmountRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountApplicationService accountApplicationService;

    public AccountController(AccountApplicationService accountApplicationService) {
        this.accountApplicationService = accountApplicationService;
    }

    @GetMapping("/{accountId}")
    public Double getBalance(@PathVariable("accountId") String accountId) {
        return accountApplicationService.getBalance(Long.valueOf(accountId));
    }

    @PutMapping("/transfer-to/{accountId}")
    public void transferTo(@PathVariable("accountId") String accountId, @RequestBody TransferAmountRequest transferAmountRequest) throws Exception {

        accountApplicationService.transferTo(Long.valueOf(accountId), transferAmountRequest);
    }

    @PostMapping("/account-transaction/{accountId}")
    public List<AccountTransactionDto> getAccountTransactionsMore(@PathVariable("accountId") Long accountId, @RequestBody GetAccountTransactionRequest getAccountTransactionRequest) {

        return accountApplicationService.getAccountTransactionsMore(accountId, getAccountTransactionRequest);
    }

}
