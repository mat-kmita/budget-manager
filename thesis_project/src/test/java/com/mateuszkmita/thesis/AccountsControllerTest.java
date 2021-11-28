package com.mateuszkmita.thesis;

import com.mateuszkmita.thesis.core.service.AccountServiceInterface;
import com.mateuszkmita.thesis.external.controller.AccountsController;
import com.mateuszkmita.thesis.external.controller.mapper.AccountUpdateMapper;
import com.mateuszkmita.thesis.external.repository.AccountRepositoryInterface;
import com.mateuszkmita.thesis.model.Account;
import com.mateuszkmita.thesis.model.AccountType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

@WebMvcTest(AccountsController.class)
public class AccountsControllerTest {

    @Autowired
     MockMvc mockMvc;

//    public AccountsControllerTest(MockMvc mockMvc) {
//        this.mockMvc = mockMvc;
//    }

    @MockBean
    AccountServiceInterface accountService;
    @MockBean
    AccountRepositoryInterface accountRepository;
    @MockBean
    AccountUpdateMapper accountUpdateMapper;

    private static final Account ACCOUNT_RECORD_1 = new Account(1, "Account #1", AccountType.CASH, "Cash", 0);
    private static final Account ACCOUNT_RECORD_2 = new Account(2, "Account #2", AccountType.CHECKING, "Checking account", 0);
    private static final Account ACCOUNT_RECORD_3 = new Account(3, "Account #3", AccountType.SAVING, "Savings account", 0);

    @Test
    public void getAllAccountsSuccessful() throws Exception {
        List<Account> accounts = List.of(ACCOUNT_RECORD_1, ACCOUNT_RECORD_2, ACCOUNT_RECORD_3);

        Mockito.when(accountRepository.findAll()).thenReturn(accounts);

        mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/account/")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[2].name", is("Account #3")));

    }

}
