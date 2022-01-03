import {useEffect, useState} from "react"
import React from "react-dom";
import {useParams} from "react-router-dom"
import {PageHeader, Skeleton, Button, Row, Statistic, Popconfirm, message} from "antd"
import {useSelector, useDispatch} from "react-redux"
import {deleteAccount, editAccount} from './accountsSlice'
import TransactionsView from "../transactions/Transactions"
import AccountEditForm from "../accounts/AccountEditForm"
import {useNavigate} from 'react-router-dom'

const AccountView = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const accountsStatus = useSelector(state => state.accounts.status)

    const {id} = useParams()

    const [account, setAccount] = useState(null)
    const findAccountById = useSelector(state => {
        return state.accounts.accounts.find(acc => acc.id == id)
    })
    useEffect(() => {
        if (accountsStatus === 'succeeded') {
            setAccount(findAccountById)
        }
    }, [id, accountsStatus, findAccountById])

    const handleAccountDelete = async (accountId) => {
        try {
            await dispatch(deleteAccount(accountId)).unwrap()
            navigate('/')
        } catch (err) {
            message.error({
                content: `An error occured while deleting transaction: ${err.message}`,
                dismiss: 5
            })
        } finally {

        }
    }

    const [isEditFormVisible, setEditFormVisible] = useState(false)
    const handleAccountEdit = async (values) => {
        try {
            await dispatch(editAccount({
                accountId: account.id,
                data: values
            })).unwrap();
            message.success({
                content: "Account has been updated",
                dismiss: 5
            })
        } catch (err) {
            message.error({
                content: `An error occured while updating transaction: ${err.message}`,
                dismiss: 5
            })
        } finally {
            setEditFormVisible(false)
        }
    }

    return (
        <>
            <AccountEditForm
                visible={isEditFormVisible}
                handleCancel={() => setEditFormVisible(false)}
                handleCreate={handleAccountEdit}
                account={account}/>
            <Skeleton loading={accountsStatus !== 'succeeded'}>
                <PageHeader
                    title={account?.name}
                    subTitle={account?.description}
                    extra={[
                        <Button primary onClick={() => setEditFormVisible(true)}>Edit account</Button>,
                        <Popconfirm
                            title="Are you sure to delete this account? All transactions on this account will be lost"
                            onConfirm={() => handleAccountDelete(account.id)}
                            okText="Yes"
                            cancelText="Cancel">
                            <Button danger>Delete account</Button>
                        </Popconfirm>
                    ]}>
                    <Row>
                        <Statistic title="Account type" value={account?.accountType}/>
                        <Statistic
                            title="Balance"
                            value={account?.balance}
                            prefix="$"
                            style={{
                                margin: '0 32px',
                            }}
                        />
                    </Row>
                </PageHeader>
                <TransactionsView/>
            </Skeleton>
        </>
    );
}

export default AccountView;