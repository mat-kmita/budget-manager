import { configureStore } from '@reduxjs/toolkit'

import accountsReducer from './features/accounts/accountsSlice.js'
import categoriesReducer from './features/categories/categoriesSlice'
import transactionsReducer from './features/transactions/transactionsSlice'

export default configureStore({
    reducer: {
        accounts: accountsReducer,
        categories: categoriesReducer,
        transactions: transactionsReducer
    }
})
