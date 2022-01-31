import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import axios from "axios";

const initialState = {
    accounts: [],
    status: 'idle',
    error: null
}

const accountsSlice = createSlice({
    name: 'accounts',
    initialState,
    reducers: {
        accountAdded(state, action) {
            state.accounts.push(action.payload)
        },
    },
    extraReducers(builder) {
        builder
            .addCase(fetchAccounts.pending, (state, action) => {
                state.status = 'loading'
            })
            .addCase(fetchAccounts.fulfilled, (state, action) => {
                state.status = 'succeeded'
                state.accounts = action.payload
                state.accounts.sort((el1, el2) => el1.id >= el2.id)
            })
            .addCase(fetchAccounts.rejected, (state, action) => {
                state.status = 'failed'
                state.error = action.error.message
            })
            .addCase(deleteAccount.fulfilled, (state, action) => {
                state.accounts = state.accounts.filter(acc => acc.id !== action.payload)
            })
            .addCase(addNewAccount.fulfilled, (state, action) => {
                state.accounts.push(action.payload)
            })
            .addCase(editAccount.fulfilled, (state, action) => {
                state.accounts = state.accounts.map(acc => acc.id === action.payload.id ? action.payload : acc)
            })
    }
})

export const {accountAdded, accountDeleted} = accountsSlice.actions

export default accountsSlice.reducer

export const fetchAccounts = createAsyncThunk('accounts/fetchAccounts', async () => {
    const response = await axios('http://localhost:8080/api/v1/account/')
    return response.data
})

export const deleteAccount = createAsyncThunk('accounts/deleteAccount', async (accountId) => {
    await axios.delete(`http://localhost:8080/api/v1/account/${accountId}/`)
    return accountId
})

export const addNewAccount = createAsyncThunk('accounts/addNewAccount', async (account) => {
    const requestData = {
        ...account,
        initialBalance: account.initialBalance * 100
    }
    const response = await axios.post("http://localhost:8080/api/v1/account/", requestData);
    return response.data
})

export const editAccount = createAsyncThunk('accounts/editAccount', async ({accountId, data}) => {
    const response = await axios.put(`http://localhost:8080/api/v1/account/${accountId}/`, data)
    return response.data
})

export const selectAllAccounts = state => state.accounts.accounts