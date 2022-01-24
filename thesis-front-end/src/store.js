import { configureStore } from '@reduxjs/toolkit'

import accountsReducer from './features/accounts/accountsSlice.js'
import categoriesReducer from './features/categories/categoriesSlice'
import transactionsReducer from './features/transactions/transactionsSlice'
import transfersReducer from './features/transfers/transfersSlice'
import budgetsReducer from './features/budgets/budgetsSlice'
import reportsReducer from './features/reports/reportsSlice'

export default configureStore({
    reducer: {
        accounts: accountsReducer,
        categories: categoriesReducer,
        transactions: transactionsReducer,
        transfers: transfersReducer,
        budgets: budgetsReducer,
        reports: reportsReducer
    }
})
