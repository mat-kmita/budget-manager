import {createSlice, createAsyncThunk} from '@reduxjs/toolkit'
import axios from "axios";

const initialState = {
    budgets: [],
    budgetsCategories: {},
    status: 'idle',
    error: {}
}

const budgetsSlice = createSlice({
    name: 'budgets',
    initialState,
    reducers: {
        addBudgetCategory: (state, action) => {
            state.budgets = state.budgets.map(budget => {})
            state.budgetsCategories.push(action.payload)
        }
    },
    extraReducers(builder) {
        builder
            .addCase(fetchBudgets.pending, (state, action) => {
                state.status = 'loading'
            })
            .addCase(fetchBudgets.fulfilled, (state, action) => {
                state.budgets = action.payload
            })
            .addCase(fetchBudgets.rejected, (state, action) => {
                state.status = 'failed'
                state.error = parseInt(action.error.message)
            })
            .addCase(createBudget.fulfilled, (state, action) => {
                console.dir(action)
                state.budgets.push(action.payload)
            })
            .addCase(fetchBudgetsCategories.fulfilled, (state, action) => {
                state.budgetsCategories[action.payload.budgetId] = action.payload.categories.filter(x => x.category.id !== 1).sort((x1, x2) => x1.id > x2.id)
            })
    }
})

export default budgetsSlice.reducer

export const fetchBudgets = createAsyncThunk('budgets/fetchBudgets', async () => {
    try {
        const response = await axios("http://localhost:8080/api/v1/budget/")
        return response.data
    } catch (err) {
        throw Error(err.response.status)
    }
})

export const fetchBudgetsCategories = createAsyncThunk("budgets/fetchCategories", async (data) => {
    const response = await axios(`http://localhost:8080/api/v1/budget/${data.budgetId}/category/`)
    return {
        categories: response.data,
        budgetId: data.budgetId
    }
})

export const createBudget = createAsyncThunk("budgets/createBudget", async (data) => {
    const reponse = await axios.post('http://localhost:8080/api/v1/budget/', data)
    return reponse.data
})