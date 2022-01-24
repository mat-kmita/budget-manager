import {useEffect, useState} from "react"
import React from "react-dom";
import {Route, Routes, Link} from 'react-router-dom'
import {Layout, Menu, message} from 'antd';
import {PlusOutlined} from "@ant-design/icons"
import {useNavigate} from 'react-router-dom'

import {useSelector, useDispatch} from 'react-redux'
import {selectAllAccounts, fetchAccounts, addNewAccount} from './features/accounts/accountsSlice'

import AccountView from "./features/accounts/AccountView"
import NewAccountForm from "./features/accounts/NewAccountForm"
import BudgetsView from "./features/budgets/BudgetsView"
import ReportsView from "./features/reports/Reports"

const {Header, Content, Sider} = Layout;
const {SubMenu} = Menu;


const App = () => {
    const dispatch = useDispatch()
    const navigate = useNavigate()

    const [newAccountFormVisible, setNewAccountFormVisible] = useState(false)

    const [accounts, setAccounts] = useState([])
    const allAccounts = useSelector(selectAllAccounts)
    const accountsStatus = useSelector(state => state.accounts.status)
    const accountsError = useSelector(state => state.accounts.error)
    useEffect(() => {
        if (accountsStatus === 'idle') {
            dispatch(fetchAccounts())
        } else if (accountsStatus === 'failed') {
            message.error({
                content: `An error occured: ${accountsError}`
            })
        } else if (accountsStatus === 'succeeded') {
            setAccounts(allAccounts)
        }
    }, [dispatch, accountsStatus, allAccounts, accountsError])

    const handleNewAccountFormShow = () => {
        setNewAccountFormVisible(true)
    }

    const handleNewAccountFormCancel = () => {
        setNewAccountFormVisible(false);
    }

    const handleNewAccount = async (values) => {
        try {
            const data = await dispatch(addNewAccount(values)).unwrap()
            message.success({
                content: 'Created new account',
                dismiss: 5
            })
            navigate(`/account/${data.id}/transaction`)
        } catch (err) {
            message.error({
                content: `Error occured: ${err.message}`
            }) 
        } finally {
            setNewAccountFormVisible(false)
        }
    }

    return (
        <>
            <NewAccountForm
                visible={newAccountFormVisible}
                handleCancel={handleNewAccountFormCancel}
                handleCreate={handleNewAccount}/>
            <Layout style={{minHeight: '100vh'}}>
                <Sider>
                    <div className="logo"/>
                    <Menu theme="dark" defaultSelectedKeys={['budget']} mode="inline">
                        <Menu.Item key="budget">
                            <Link to="/">Budget</Link>
                        </Menu.Item>
                        <Menu.Item key="reports">
                            <Link to="/reports" id="reports-link">Reports</Link>
                        </Menu.Item>
                        {accounts.map(account => (
                            <SubMenu key={`submenu-${account.id}`} title={account.name}>
                                <Menu.Item key={`transactions-${account.id}`}>
                                    <Link to={`/account/${account.id}/transaction`}
                                          id={`transactions-link-${account.id}`}>Transactions</Link>
                                </Menu.Item>
                                <Menu.Item key={`tranfers-${account.id}`}>
                                    <Link to={`/account/${account.id}/transfer`}
                                          id={`transfers-link-${account.id}`}>Transfers</Link>
                                </Menu.Item>
                            </SubMenu>
                        ))}
                        <Menu.Item key="new-account" onClick={handleNewAccountFormShow}>
                            <PlusOutlined /> Create new account
                        </Menu.Item>
                    </Menu>
                </Sider>
                <Layout className="site-layout">
                    <Header className="site-layout-background" style={{padding: -1}}/>
                    <Content style={{margin: '-1 16px'}}>
                        <div className="site-layout-background" style={{padding: 23, minHeight: 360}}>
                            <Routes>
                                <Route exact path="/" element={<BudgetsView />}/>
                                <Route path="/account/:id/*" element={<AccountView/>}/>
                                <Route path="/reports" element={<ReportsView/>}/>
                            </Routes>
                        </div>
                    </Content>
                </Layout>
            </Layout>
        </>
    )
}
export default App