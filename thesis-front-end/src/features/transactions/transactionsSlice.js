import {createSlice, createAsyncThunk} from '@reduxjs/toolkit'
import axios from "axios";
import qs from 'qs'

const initialState = {
    transactions: [],
    length: 0,
    status: 'idle',
    error: null
}

const transactionsSlice = createSlice({
    name: 'transactions',
    initialState,
    reducers: {
        transactionAdded(state, action) {
            state.transactions.push(action.payload)
            state.transactions.length += 1
        }
    },
    extraReducers(builder) {
        builder
            .addCase(fetchTransactions.pending, (state, action) => {
                state.status = 'loading'
            })
            .addCase(fetchTransactions.fulfilled, (state, action) => {
                state.status = 'succeeded'
                state.transactions = action.payload.data
                state.length = action.payload.length
            })
            .addCase(addNewTransaction.fulfilled, (state, action) => {
                state.transactions.push(action.payload)
                state.length += 1
            })
            .addCase(editTransaction.fulfilled, (state, action) => {
                state.transactions = state.transactions.filter(t => t.id !== action.payload.id)
                state.transactions.push(action.payload)
            })
            .addCase(deleteTransaction.fulfilled, (state, action) => {
                state.transactions = state.transactions.filter(t => t.id !== action.payload)
            })
    }
})

export const {transactionAdded, transactionUpdated, transactionDeleted} = transactionsSlice.actions

export default transactionsSlice.reducer

export const fetchTransactions = createAsyncThunk('transcations/fetchTransactions', async fetchData => {
    const requestParams = {
        sortDirection: 'DESC',
        sortField: 'date',
        ...fetchData.pagination
    }
    const response = await axios(`http://localhost:8080/api/v1/account/${fetchData.id}/transaction/?${qs.stringify(requestParams)}`)
    return {
        data: response.data.data,
        length: response.data.allDataSize
    }
})

export const editTransaction = createAsyncThunk('transactions/editTransaction', async data => {
    console.log('IN EDIT TRANSACTION THUNK')
    const {id, ...payload} = data
    const date = new Date(payload.date)
    const dayPart = date.getDate() < 10 ? `0${date.getDate()}` : `${date.getDate()}`
    const monthPart = date.getMonth() + 1 < 10 ? `0${date.getMonth() + 1}` : `${date.getMonth() + 1}`
    const yearPart = date.getFullYear()
    const formatedDate = `${dayPart}-${monthPart}-${yearPart}`
    payload.date = formatedDate
    const result = await axios.put(`http://localhost:8080/api/v1/transaction/${id}/`, payload)
    return result
})

export const deleteTransaction = createAsyncThunk('transactions/deleteTransaction', async id => {
    await axios.delete(`http://localhost:8080/api/v1/transaction/${id}/`)
    return id;
})

export const addNewTransaction = createAsyncThunk('transactions/addNewTransaction', async data => {
    const date = new Date(data.payload.date)
    const dayPart = date.getDate() < 10 ? `0${date.getDate()}` : `${date.getDate()}`
    const monthPart = date.getMonth() + 1 < 10 ? `0${date.getMonth() + 1}` : `${date.getMonth() + 1}`
    const yearPart = date.getFullYear()
    const formatedDate = `${dayPart}-${monthPart}-${yearPart}`
    data.payload.date = formatedDate
    const response = await axios.post(`http://localhost:8080/api/v1/account/${data.accountId}/transaction/`, data.payload);
    return response.data
})

export const selectAllTransactions = state =>
    state.transactions.transactions

export const selectTransactionsCount = state =>
    state.transactions.length