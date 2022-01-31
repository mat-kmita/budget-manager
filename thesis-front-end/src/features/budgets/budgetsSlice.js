import {createSlice, createAsyncThunk} from '@reduxjs/toolkit'
import axios from "axios";

const initialState = {
    budgets: [],
    budgetCategories: [],
    status: 'idle',
    budgetCategoriesStatus: 'idle',
    activeBudgetId: null,
    error: {}
}

const budgetsSlice = createSlice({
    name: 'budgets',
    initialState,
    reducers: {
        updatedBudgetCategoryName: (state, action) => {
            state.budgetCategories = state.budgetCategories.map(bc => {
                if (bc.category.id === action.payload.categoryId) {
                    return {
                        ...bc,
                        category: {
                            ...bc.category,
                            name: action.payload.categoryName
                        }
                    }
                } else {
                    return bc
                }
            })
        },
        setActiveBudgetId: (state, action) => {
            state.activeBudgetId = action.payload.budgetId
        },
        deleteBudgetCategory: (state, action) => {
            state.budgetCategories = state.budgetCategories.filter(bc => bc.category.id !== action.payload.categoryId)
        }
    },
    extraReducers(builder) {
        builder
            .addCase(fetchBudgets.pending, (state, action) => {
                state.status = 'loading'
            })
            .addCase(fetchBudgets.fulfilled, (state, action) => {
                state.status = 'succeeded'
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
            .addCase(fetchBudgetCategories.pending, (state, action) => {
                state.budgetCategoriesStatus = 'loading'
            })
            .addCase(fetchBudgetCategories.fulfilled, (state, action) => {
                state.budgetCategoriesStatus = 'succeeded'
                state.budgetCategories = action.payload.categories.filter(x => x.category.id !== 1).sort((x1, x2) => x1.id > x2.id)
            })
    }
})

export const {updatedBudgetCategoryName,setActiveBudgetId, deleteBudgetCategory} = budgetsSlice.actions

export default budgetsSlice.reducer

export const fetchBudgets = createAsyncThunk('budgets/fetchBudgets', async () => {
    try {
        const response = await axios("http://localhost:8080/api/v1/budget/")
        return response.data
    } catch (err) {
        throw Error(err.response.status)
    }
})

export const fetchBudgetCategories = createAsyncThunk("budgets/fetchCategories", async (data) => {
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

export const budgetMoney = createAsyncThunk("budgets/budgetMoney", async (data) => {
    const response = await axios.put(`http://localhost:8080/api/v1/budgetCategory/budget/${data.budgetId}/category/${data.categoryId}/`, {
        budgetedAmount: data.amount
    })
    return {
        ...response.data,
        budgetId: data.budgetId
    }
})