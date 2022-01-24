import {createSlice, createAsyncThunk} from '@reduxjs/toolkit'
import axios from "axios";
import qs from 'qs'

const initialState = {
    transfers: {},
    status: 'idle',
    error: null
}

const transfersSlice = createSlice({
    name: 'transfers',
    initialState,
    reducers: {
    },
    extraReducers(builder) {
        builder
            .addCase(fetchTransfers.pending, (state, action) => {
                state.status = 'loading'
            })
            .addCase(fetchTransfers.fulfilled, (state, action) => {
                console.log('in fetchtransfers fulflled')
                console.dir(action)
                state.status = 'succeeded'
                state.transfers[action.payload.id] = action.payload.data.data
                state.length = action.payload.length
            })
    }
})

export default transfersSlice.reducer

export const fetchTransfers = createAsyncThunk('transfers/fetchTransfers', async fetchData => {
    const requestParams = {
        sortDirection: 'DESC',
        sortField: 'date',
        ...fetchData.pagination
    }
    const response = await axios(`http://localhost:8080/api/v1/account/${fetchData.id}/transfer/?${qs.stringify(requestParams)}`)
    return {
        id: fetchData.id,
        data: response.data
    }
})