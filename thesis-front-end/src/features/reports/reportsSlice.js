import {createSlice, createAsyncThunk} from '@reduxjs/toolkit'
import axios from "axios";
import qs from 'qs'

const initialState = {
    incomesHeatMapData: [],
    outcomesHeatMapData: [],
    expensesByCategoryData: [],
    status: 'idle',
    expensesByCategoryStatus: 'idle',
    error: null
}

const transactionsSlice = createSlice({
    name: 'reports',
    initialState,
    reducers: {
    },
    extraReducers(builder) {
        builder
            .addCase(fetchHeatMaps.pending, (state, action) => {
                state.status = 'loading'
                console.log('pending heat maps')
            })
            .addCase(fetchHeatMaps.fulfilled, (state, action) => {
                state.status = 'succeeded'
                state.incomesHeatMapData = action.payload.incomes.amountsByDate
                state.outcomesHeatMapData = action.payload.outcomes.amountsByDate
            })
            .addCase(fetchExpensesByCategory.pending, (state, action) => {
                state.expensesByCategoryStatus = 'loading'
            })
            .addCase(fetchExpensesByCategory.fulfilled, (state, action) => {
                state.expensesByCategoryStatus = 'succeeded'
                state.expensesByCategoryData = action.payload
            })
        }
})

export default transactionsSlice.reducer

export const fetchHeatMaps = createAsyncThunk('reports/fetchHeatMaps', async data => {
    const outcomesResponse = await axios(`http://localhost:8080/api/v1/report/heatmap/outcomes/?${qs.stringify(data)}`)
    const incomesResponse = await axios(`http://localhost:8080/api/v1/report/heatmap/incomes/?${qs.stringify(data)}`)
    return {
        incomes: incomesResponse.data,
        outcomes: outcomesResponse.data
    }
})

export const fetchExpensesByCategory = createAsyncThunk('reports/fetchExpensesByCategory', async data => {
    const response = await axios(`http://localhost:8080/api/v1/report/expenses-by-category/?${qs.stringify(data)}`)
    return response.data
})
