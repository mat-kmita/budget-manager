import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom"
import 'antd/dist/antd.css';
import {Table, Popconfirm, message} from "antd"
import {DeleteTwoTone, EditTwoTone} from "@ant-design/icons"

import {useSelector, useDispatch} from 'react-redux'
import {
    selectAllTransactions,
    fetchTransactions,
    deleteTransaction,
    editTransaction
} from './transactionsSlice'
import {fetchAccounts} from "../accounts/accountsSlice"

import AddTransactionComponent from './AddTransactionComponent'
import TransactionEditForm from './TransactionEditForm'

const {Column} = Table;

const TransactionsView = () => {
    return (
        <div>
            <AddTransactionComponent/>
            <TransactionsTable/>
        </div>
    )
}

const defaultPagination = {
    current: 1,
    pageSize: 3,
    showSizeChanger: true,
    showQuickJumper: true,
    pageSizeOptions: [3, 5, 10, 15]
}

const TransactionsTable = (() => {
    const getFetchArgsFromPagination = pagination => {
        console.log('Called getFetchArgs from pagination: ')
        console.dir(pagination)
        return {
            pagination: {
                length: pagination.pageSize,
                page: pagination.current - 1
            }
        }
    }

    const dispatch = useDispatch()


    const {id} = useParams()

    useEffect(() => {
        console.log('ID CHANGED: ', id)
        const {current, ...rest} = pagination
        const newPagination = {
            current: 1,
            ...rest
        }
        console.log('pagination new: ')
        console.dir(newPagination)
        if (transactionStatus !== 'idle' && transactionStatus !== 'loading') {
            dispatch(fetchTransactions({
                id,
                ...getFetchArgsFromPagination(newPagination)
            }))
            setPagination(newPagination)
        }

        console.log('changed id in transactions table')
        console.dir(pagination)
    }, [id])


    const transactions = useSelector(selectAllTransactions)

    const [pagination, setPagination] = useState(defaultPagination)
    const total = useSelector(state => state.transactions.length)

    const transactionStatus = useSelector(state => state.transactions.status)

    const loading = () => transactionStatus === 'loading'

    const handleTransactionDelete = async (transactionId) => {
        console.log('in delete handler')
        console.dir(pagination)
        try {
            await dispatch(deleteTransaction(transactionId)).unwrap();
            if (transactions.length === 0) {
                const newPagination = {
                    ...pagination,
                    current: pagination.current - 1
                }
                setPagination(newPagination)
            } else {
                await(dispatch(fetchTransactions({
                    id, ...getFetchArgsFromPagination(pagination)
                }))).unwrap()
            }
            await dispatch(fetchAccounts()).unwrap()
        } catch (err) {
            message.error(`Couldn't delete this transaction. Reason: ${err.message}`)
        }
    }

    const handleTableChange = (newPagination, sorter) => {
        setPagination(newPagination)
        console.log('in table change')
        console.dir(newPagination)
        dispatch(fetchTransactions({
            id,
            ...getFetchArgsFromPagination(newPagination)
        }))
    }

    useEffect(() => {
        if (transactionStatus === 'idle') {
            dispatch(fetchTransactions({
                id,
                ...getFetchArgsFromPagination(pagination)
            }))
        }
    }, [dispatch, transactionStatus])

    const [editorVisible, setEditorVisible] = useState(false);
    const [editedTransaction, setEditedTransaction] = useState(null)

    const handleEdit = (transaction) => {
        setEditedTransaction(transaction)
        setEditorVisible(true)
    }

    const handleCancel = () => {
        setEditorVisible(false)
    }

    const handleEdited = async (values) => {
        console.log('handling edit')
        console.dir(values)
        try {
            // myThunk(values)
            await dispatch(editTransaction(values)).unwrap()
            await dispatch(fetchAccounts()).unwrap()
            message.success({
                content: "Transaction has been edited",
                dismiss: 5
            })
        } catch (err) {
            message.error({
                content: `An error occured while editing transaction: ${err.message}`,
                dismiss: 5
            })
        } finally {
            setEditorVisible(false)
        }
    }

    return (
        <>
            <TransactionEditForm
                visible={editorVisible}
                transaction={editedTransaction}
                onCancel={handleCancel}
                onCreate={handleEdited}/>
            <Table
                dataSource={transactions}
                rowKey={record => record.id}
                // columns={columns}
                pagination={{...pagination, total: total}}
                loading={loading}
                onChange={handleTableChange}
                size="small">
                <Column
                    title="Id"
                    dataIndex="id"
                    key="id"/>
                <Column
                    title="Date"
                    dataIndex="date"
                    key="date"/>
                <Column
                    title="Payee"
                    dataIndex="payee"
                    key="payee"/>
                <Column
                    title="Category"
                    dataIndex="category"
                    key="category"/>
                <Column
                    title="Memo"
                    dataIndex="memo"
                    key="memo"/>
                <Column
                    title="Amount"
                    dataIndex="amount"
                    key="amount"/>
                <Column
                    title="Delete"
                    key="delete"
                    render={(text, record) => (
                        <Popconfirm
                            title="Are you sure to delete this transaction?"
                            onConfirm={() => handleTransactionDelete(record.id)}
                            okText="Yes"
                            cancelText="Cancel">
                            <DeleteTwoTone danger/>
                        </Popconfirm>
                    )}/>
                <Column
                    title="Edit"
                    key="edit"
                    render={(text, record) => (
                        <a href="#" onClick={() => handleEdit(record)}><EditTwoTone/></a>
                    )}/>
            </Table>
        </>
    )
})

export default TransactionsView;