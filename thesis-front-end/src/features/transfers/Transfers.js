import {useParams} from "react-router-dom"
import React, {useEffect, useState} from "react";
import {Table, Button, Popconfirm, message} from "antd"
import {DeleteTwoTone, EditTwoTone} from "@ant-design/icons"
import {useDispatch, useSelector} from "react-redux"
import {fetchAccounts} from "../accounts/accountsSlice"
import {fetchTransfers, makeTransfer, editTransfer, deleteTransfer} from "./transfersSlice"
import MakeTransferForm from "./MakeTransferForm"
import EditTransferForm from "./EditTransferForm"

const {Column} = Table;

const TransfersView = () => {

    const dispatch = useDispatch();
    const {id} = useParams()

    const fetchTransfersData = async (accountId, page, length) => {
        await dispatch(fetchTransfers({
            id: accountId,
            pagination: {
                page: page - 1,
                length: length
            }
        })).unwrap()
    }

    const transfers = useSelector(state => state.transfers.transfers)
    const transfersStatus = useSelector(state => state.transfers.status)
    const transfersLength = useSelector(state => state.transfers.length)

    const account = useSelector(state => state.accounts.accounts.find(a => a.id === parseInt(id)))
    const accounts = useSelector(state => state.accounts.accounts)

    const [pagination, setPagination] = useState({
        current: 1,
        showSizeChanger: true,
        showQuickJumper: true
    })
    const fetchCurrentTransfersPage = async () => await fetchTransfersData(id, pagination.current, pagination.pageSize)

    // Creating transfers
    const [makeTransferFormVisible, setMakeTransferFormVisible] = useState(false)
    const handleMakeTransfer = async (values) => {
        try {
            await dispatch(makeTransfer({
                accountId: id,
                transfer: values
            })).unwrap()
            await fetchCurrentTransfersPage()
            await dispatch(fetchAccounts()).unwrap()
            message.success('Successfuly made a transfer', 5)
        } catch (e) {
            message.error(`There was an error while creating transfer: ${e.message}`, 10)
        } finally {
            setMakeTransferFormVisible(false)
        }
    }

    // Editing transfers
    const [editTransferFormVisible, setEditTransferFormVisible] = useState(false)
    const [editedTransfer, setEditedTransfer] = useState(null)
    const handleEditTransfer = async (values) => {
        try {
            await dispatch(editTransfer({
                ...values,
                id: editedTransfer.id
            })).unwrap()
            await fetchCurrentTransfersPage()
            await dispatch(fetchAccounts())
            message.success(`Successfuly edited transfer with ID ${editedTransfer.id}!`, 5)
        } catch (e) {
            message.error(`There was an error while editing transfer with ID ${editTransfer.id}: ${e.message}`, 10)
        } finally {
            setEditTransferFormVisible(false)
        }
    }

    // Deleting transfers
    const handleDeleteTransfer = async (transfer) => {
        try {
            await dispatch(deleteTransfer({
                transferId: transfer.id
            })).unwrap()
            await fetchCurrentTransfersPage()
            await dispatch(fetchAccounts()).unwrap()
            message.success(`Successfuly deleted transfer with ID ${transfer.id}`, 5)
        } catch (e) {
            message.error(`There was an error while deleting tranfer with ID ${transfer.id}: ${e.message}`, 10)
        }
    }

    // Handling pagination
    const handleTableChange = (newPagination, sorter) => {
        setPagination(newPagination)
    }
    useEffect(() => {
        setPagination({
            ...pagination,
            current: 1
        })
    }, [id, dispatch])
    useEffect(() => {
        try {
            fetchCurrentTransfersPage()
        } catch (e) {

        }
    }, [pagination])

    return (
        <>
            <MakeTransferForm
                visible={makeTransferFormVisible}
                onCreate={handleMakeTransfer}
                onCancel={() => {
                    setMakeTransferFormVisible(false);
                }}/>
            <Button
                type="primary"
                onClick={() => {
                    setMakeTransferFormVisible(true)
                }}
                disabled={account?.balance === 0}>
                Make a transfer
            </Button>

            <EditTransferForm
                visible={editTransferFormVisible}
                onCancel={() => {
                    setEditTransferFormVisible(false)
                    setEditedTransfer(null)
                }}
                onCreate={handleEditTransfer}
                transfer={editedTransfer}/>

            <Table
                dataSource={transfers}
                rowKey={record => record.id}
                pagination={{...pagination, total: transfersLength}}
                loading={transfersStatus === 'idle' || transfersStatus === 'loading'}
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
                    title="To/from account"
                    key="payee"
                    render={(text, record) => {
                        return <p> {accounts.find(a => a.id === record.payeeId).name} </p>
                    }}/>
                <Column
                    title="Memo"
                    dataIndex="memo"
                    key="memo"/>
                <Column
                    title="Amount"
                    key="amount"
                    render={(text, record) => {
                        return <p> {record.amount / 100} </p>
                    }}/>
                <Column
                    title="Delete"
                    key="delete"
                    render={(text, record) => (
                        <Popconfirm
                            title="Are you sure to delete this transfer?"
                            onConfirm={() => {
                                handleDeleteTransfer(record)
                            }}
                            okText="Yes"
                            cancelText="Cancel">
                            <DeleteTwoTone danger/>
                        </Popconfirm>
                    )}/>
                <Column
                    title="Edit"
                    key="edit"
                    render={(text, record) => (
                        <Button type="text"
                                onClick={() => {
                                    setEditedTransfer(record)
                                    setEditTransferFormVisible(true)
                                }}>
                            <EditTwoTone/>
                        </Button>
                    )}/>
            </Table>
        </>
    )
}

export default TransfersView