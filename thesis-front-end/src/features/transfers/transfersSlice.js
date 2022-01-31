import {createSlice, createAsyncThunk} from '@reduxjs/toolkit'
import axios from "axios";
import qs from 'qs'

const initialState = {
    transfers: [],
    length: 0,
    status: 'idle',
    error: null
}

const transfersSlice = createSlice({
    name: 'transfers',
    initialState,
    reducers: {},
    extraReducers(builder) {
        builder
            .addCase(fetchTransfers.pending, (state, action) => {
                state.status = 'loading'
            })
            .addCase(fetchTransfers.fulfilled, (state, action) => {
                state.status = 'succeeded'
                state.transfers = action.payload.data
                state.length = action.payload.allDataSize
            })
            .addCase(fetchTransfers.rejected, (state, action) => {
                state.status = 'failed'
            })
    }
})

export default transfersSlice.reducer

export const fetchTransfers = createAsyncThunk('transfers/fetchTransfers', async fetchData => {
    const requestParams = {
        sortDirection: 'DESC',
        sortField: 'date',
        page: fetchData.pagination.page,
        length: fetchData.pagination.length
    }
    const response = await axios(`http://localhost:8080/api/v1/account/${fetchData.id}/transfer/?${qs.stringify(requestParams)}`)
    return response.data
})

export const makeTransfer = createAsyncThunk('transfer/makeTransfer', async data => {
    const date = new Date(data.transfer.date)
    const dayPart = date.getDate() < 10 ? `0${date.getDate()}` : `${date.getDate()}`
    const monthPart = date.getMonth() + 1 < 10 ? `0${date.getMonth() + 1}` : `${date.getMonth() + 1}`
    const yearPart = date.getFullYear()
    const formatedDate = `${dayPart}-${monthPart}-${yearPart}`
    data.transfer.date = formatedDate
    data.transfer.amount = data.transfer.amount * 100
    const response = await axios.post(`http://localhost:8080/api/v1/account/${data.accountId}/transfer/`, data.transfer)
    return {
        from: data.accountId,
        to: data.transfer.toAccountId,
        response: response
    }
})

export const editTransfer = createAsyncThunk('transfer/editTransfer', async data => {
    const date = new Date(data.date)
    const dayPart = date.getDate() < 10 ? `0${date.getDate()}` : `${date.getDate()}`
    const monthPart = date.getMonth() + 1 < 10 ? `0${date.getMonth() + 1}` : `${date.getMonth() + 1}`
    const yearPart = date.getFullYear()
    const formatedDate = `${dayPart}-${monthPart}-${yearPart}`
    data.date = formatedDate
    data.amount = data.amount * 100
    const response = await axios.put(`http://localhost:8080/api/v1/transfer/${data.id}/`, data)
    return response.data
})

export const deleteTransfer = createAsyncThunk('transfer/deleteTransfer', async data => {
    const response = await axios.delete(`http://localhost:8080/api/v1/transfer/${data.transferId}/`)
    return response
})