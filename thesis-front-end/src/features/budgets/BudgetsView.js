import {useState, useEffect} from "react"
import {useDispatch, useSelector} from "react-redux"
// import {message} from "antd"
import {fetchBudgets, createBudget, fetchBudgetsCategories} from "./budgetsSlice"
import {selectAllCategories, fetchCategories, createCategory} from "../categories/categoriesSlice"
import {PageHeader, Button, Popconfirm, Statistic, Row, Table, Tooltip, Skeleton, message} from 'antd';
import {DeleteTwoTone, EditTwoTone, DollarTwoTone} from "@ant-design/icons"
import moment from "moment"
import NewCategoryForm from "../categories/NewCategoryForm"
import EditCategoryForm from "../categories/EditCategoryForm"
import BudgetingForm from "./BudgetingForm"

const {Column} = Table

const BudgetsView = () => {
    const dispatch = useDispatch();

    const [budgetId, setBudgetId] = useState(null)
    const allBudgets = useSelector(state => state.budgets.budgets)
    const currentBudget = useSelector(state => state.budgets.budgets.find(b => b.id === budgetId))
    const currentMonthBudget = useSelector(state => state.budgets.budgets.find(b => {
        const currentDate = new Date()
        return b.year == currentDate.getFullYear() && b.month == currentDate.getMonth() + 1
    }))
    const currentBudgetCategories = useSelector(state => state.budgets.budgetsCategories[budgetId])

    useEffect(() => {
        console.log(`changed budgetId to ${budgetId}`)

        if (budgetId === null) {
            console.log('dispatching action to fetch budgets')
            if (allBudgets.length === 0) {
                dispatch(fetchBudgets())
            }
        } else {
            if (currentBudgetCategories === undefined) {
                dispatch(fetchBudgetsCategories({
                    budgetId: budgetId
                }))
            }
        }
    }, [budgetId])

    useEffect(() => {
        console.log(`currentMonthBudget hook. it is now ${currentMonthBudget}`)
        if (budgetId == null && currentMonthBudget !== undefined) {
            setBudgetId(currentMonthBudget.id)
        }
    }, [currentMonthBudget])

    useEffect(() => {
        console.log(`currentBudget is now ${currentBudget}`)
    }, [currentBudget])

    const [newCategoryFormVisible, setNewCategoryFormVisible] = useState(false);
    const handleCategoryCreate = async (values) => {
        console.log('handling new acategory')
        try {
            await dispatch(createCategory(values)).unwrap()
            dispatch(fetchBudgetsCategories({budgetId: budgetId}))
            message.success({
                content: "Category has been created",
                dismiss: 5
            })
        } catch (err) {
            message.error({
                content: `An error occured while creating category: ${err.message}`,
                dismiss: 5
            })
        } finally {
            setNewCategoryFormVisible(false)
        }
    }

    const [editedCategory, setEditedCategory] = useState(null)
    const [editCategoryFormVisible, setEditCategoryFormVisible] = useState(false)
    const handleEditCategory = async (category) => {
        setEditedCategory(category)
        setEditCategoryFormVisible(true)
    }

    const [isBudgetingFormVisible, setIsBudgetingFormVisible] = useState(false)
    const [budgetingFormData, setBudgetingFormData] = useState(null)

    const handleBudgeting = (record) => {
        console.log('in handle budgeting')
        console.dir(record)
        setBudgetingFormData({
            amount: record.amount
        })
        setIsBudgetingFormVisible(true)
    }

    return (
        <div>
            <NewCategoryForm
                visible={newCategoryFormVisible}
                onCancel={() => setNewCategoryFormVisible(false)}
                onCreate={handleCategoryCreate}
            />
            <EditCategoryForm
                visible={editCategoryFormVisible}
                onCancel={() => setEditCategoryFormVisible(false)}
                category={editedCategory}
            />
            <Skeleton active={true} loading={currentBudget === undefined}>
                <PageHeader
                    title={`${moment(currentBudget?.year + "-" + currentBudget?.month).format("MMMM")} ${currentBudget?.year}`}
                    extra={
                        <div>
                            <Button type="button" onClick={() => setNewCategoryFormVisible(true)}>Add category</Button>
                            <Button key="previousBudget" type="button"
                                    disabled={currentBudget?.previousBudgetId === null}
                                    onClick={() => setBudgetId(currentBudget?.previousBudgetId)}>Previous</Button>
                            <Button key="nextBudget" type="button" disabled={currentBudget?.nextBudgetId === null}
                                    onClick={() => setBudgetId(currentBudget?.nextBudgetId)}>Next</Button>
                        </div>}>
                    <Row>
                        <Statistic
                            title="Available"
                            prefix="$"
                            value={currentBudget?.available / 100}
                        />
                    </Row>
                </PageHeader>
            </Skeleton>
            <Table
                dataSource={currentBudgetCategories}
                rowKey={record => record.id}
                // loading={select === 'loading'}
                // onChange={handleTableChange}
                pagination={false}
                size="small">
                <Column
                    title="Edit category"
                    key="edit-category"
                    width="5%"
                    render={(text, record) => {
                        return <EditTwoTone onClick={() => handleEditCategory(record.category)}/>
                    }}/>
                <Column
                    title="Delete category"
                    key="delete-category"
                    width="5%"
                    render={(text, record) => {
                        return <Popconfirm
                            title="Are you sure to delete this category?"
                            // onConfirm={() => handleTransactionDelete(record.id)}
                            okText="Yes"
                            cancelText="Cancel">
                            <DeleteTwoTone danger/>
                        </Popconfirm>
                    }}/>
                <Column
                    title="Category"
                    dataIndex={["category", "name"]}
                    key="category.name"/>
                <Column
                    title="Budgeted"
                    key="amount"
                    render={(text, record) => {
                        return <>{record.amount / 100}</>
                    }}/>
                <Column
                    title="Spent"
                    key="spent"
                    render={(text, record) => {
                        return <>{record.spent / 100}</>
                    }}/>
                <Column
                    title="Available"
                    key="available"
                    render={(text, record) => {
                        return <>{record.available / 100}</>
                    }}/>
                <Column
                    title="Budget"
                    key="budget"
                    render={(text, record) => {
                        return <Tooltip title="Allocate money in this category">
                            <DollarTwoTone onClick={() => handleBudgeting(record)}/>
                        </Tooltip>
                    }}/>
            </Table>
            <BudgetingForm visible={isBudgetingFormVisible}
                           onCreate={() => {
                               console.log('create');
                               setIsBudgetingFormVisible(false)
                           }}
                           onCancel={() => setIsBudgetingFormVisible(false)}
                           data={budgetingFormData}/>
        </div>
    )
}

export default BudgetsView