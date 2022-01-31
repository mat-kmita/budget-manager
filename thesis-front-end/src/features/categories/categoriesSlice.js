import { createSlice, createAsyncThunk} from '@reduxjs/toolkit'
import axios from "axios";

const initialState = {
    categories: [],
    status: 'idle',
    editStatus: 'idle',
    deleteStatus: 'idle',
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
            .addCase(createCategory.fulfilled, (state, action) => {
                state.categories.push(action.payload)
            })
            .addCase(editCategory.pending, (state, action) => {
                state.editStatus = 'loading'
            })
            .addCase(editCategory.fulfilled, (state, action) => {
                state.editStatus = 'succeeded'
                state.categories = state.categories.map(c => {
                    if (c.id === action.payload.id) {
                        return action.payload
                    } else {
                        return c
                    }
                })
            })
            .addCase(deleteCategory.pending, (state, action) => {
                state.deleteStatus = 'loading'
            })
            .addCase(deleteCategory.fulfilled, (state, action) => {
                state.deleteStatus = 'succeeded'
                state.categories = state.categories.filter(c => c.id !== action.payload.categoryId)
            })
            .addCase(deleteCategory.rejected, (state, action) => {
                state.deleteStatus = 'failed'
            })
    }
})

export const {categoryAdded, categoryUpdated, categoryRemoved} = categorySlice.actions

export default categorySlice.reducer

export const fetchCategories = createAsyncThunk('categories/fetchCategories', async () => {
    const response = await axios('http://localhost:8080/api/v1/category/')
    return response.data
})

export const createCategory = createAsyncThunk('categories/createCategory', async (data) => {
    const response = await axios.post('http://localhost:8080/api/v1/category/', data)
    return response.data
})

export const editCategory = createAsyncThunk('categories/editCategory', async (data) => {
    const response = await axios.put(`http://localhost:8080/api/v1/category/${data.categoryId}/`, data.payload)
    return response.data
})

export const deleteCategory = createAsyncThunk('categories/deleteCategory', async (data) => {
    const response = await axios.delete(`http://localhost:8080/api/v1/category/${data.categoryId}/`)
    return data.categoryId
})

export const selectAllCategories = state => state.categories.categories