import { createSlice, createAsyncThunk} from '@reduxjs/toolkit'
import axios from "axios";

const initialState = {
    categories: [],
    status: 'idle',
    error: null
}

const categorySlice = createSlice({
    name: 'categories',
    initialState,
    reducers: {
        categoryAdded(state, action) {
            state.categories.push(action.payload)
        }
    },
    extraReducers(builder) {
        builder
            .addCase(fetchCategories.pending, (state, action) => {
                state.status = 'loading'
            })
            .addCase(fetchCategories.fulfilled, (state, action) => {
                state.status = 'succeeded'
                state.categories = action.payload
            })
    }
})

export const {categoryAdded, categoryUpdated, categoryRemoved} = categorySlice.actions

export default categorySlice.reducer

export const fetchCategories = createAsyncThunk('categories/fetchCategories', async () => {
    const response = await axios('http://localhost:8080/api/v1/category/')
    return response.data
})

export const selectAllCategories = state => state.categories.categories