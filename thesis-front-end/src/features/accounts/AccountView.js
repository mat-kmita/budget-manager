import {useEffect, useState} from "react"
import React from "react-dom";
import {useParams} from "react-router-dom"
import {Routes, Route} from "react-router-dom"
import {PageHeader, Skeleton, Button, Row, Statistic, Popconfirm, message} from "antd"
import {useSelector, useDispatch} from "react-redux"
import {fetchAccounts, deleteAccount, editAccount} from './accountsSlice'
import {fetchBudgets, fetchBudgetCategories} from "../budgets/budgetsSlice"
import TransactionsView from "../transactions/Transactions"
import TransfersView from "../transfers/Transfers"
import AccountEditForm from "../accounts/AccountEditForm"
import {useNavigate} from 'react-router-dom'
import NotFound from "../utils/NotFound"

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

    // Deleting accounts
    const activeBudgetId = useSelector(state => state.budgets.activeBudgetId)
    const handleAccountDelete = async (accountId) => {
        try {
            await dispatch(deleteAccount(accountId)).unwrap()
            await dispatch(fetchAccounts()).unwrap()
        } catch (err) {
            message.error({
                content: `An error occured while deleting account: ${err.message}`,
                dismiss: 5
            })
        } finally {
            navigate('/')
        }
    }

    // Editing accounts
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
            {accountsStatus === 'succeeded' && account === undefined && <NotFound/>}
            {accountsStatus === 'succeeded' && account !== undefined && (
                <>
                    <AccountEditForm
                        visible={isEditFormVisible}
                        handleCancel={() => setEditFormVisible(false)}
                        handleCreate={handleAccountEdit}
                        account={account}/>
                    <Skeleton loading={accountsStatus === 'loading'}>
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
                                    value={account?.balance / 100}
                                    prefix="$"
                                    style={{
                                        margin: '0 32px',
                                    }}
                                />
                            </Row>
                        </PageHeader>
                        <Routes>
                            <Route path="transaction" element={<TransactionsView/>}/>
                            <Route path="transfer" element={<TransfersView/>}/>
                        </Routes>
                    </Skeleton>
                </>
            )}
        </>

    );
}

export default AccountView;