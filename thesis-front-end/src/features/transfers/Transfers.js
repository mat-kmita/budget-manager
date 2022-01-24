import {useParams} from "react-router-dom"
import React, {useEffect} from "react";
import {Table, Button, Popconfirm} from "antd"
import {DeleteTwoTone, EditTwoTone} from "@ant-design/icons"
import {useDispatch, useSelector} from "react-redux"
import {fetchTransfers} from "./transfersSlice"
import MakeTransferComponent from "./MakeTransferForm"

const {Column} = Table;

const TransfersView = () => {

    const dispatch = useDispatch();

    const {id} = useParams()
    const pagination = {
        current: 1,
        showSizeChanger: true,
        showQuickJumper: true
    }

    const transfers = useSelector(state => state.transfers.transfers[id] || [])
    const allTransfers = useSelector(state => state.transfers.transfers)
    const transfersStatus = useSelector(state => state.transfers.status)
    useEffect(() => {
        if (transfersStatus === 'idle') {
            dispatch(fetchTransfers({
                id,
                pagination: {
                    length: pagination.pageSize,
                    page: pagination.current - 1
                }
            }))
        }
    }, [transfersStatus])

    useEffect(() => {
        if (!allTransfers[id]) {
            dispatch(fetchTransfers({
                id,
                pagination: {
                    length: pagination.pageSize,
                    page: pagination.current - 1
                }
            }))
        }
        console.log('abc')
    }, [id])

    return (
        <>
            <MakeTransferComponent/>
            <Table
                dataSource={transfers}
                rowKey={record => record.id}
                pagination={{...pagination, total: transfers.length}}
                // loading={loading}
                // onChange={handleTableChange}
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
                    title="To account"
                    dataIndex="payee"
                    key="payee"/>
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
                        <a href="#" onClick={() => {
                        }}><EditTwoTone/></a>
                    )}/>
            </Table>
        </>
    )
}

export default TransfersView