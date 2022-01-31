import {useState, useEffect} from "react"
import {useDispatch, useSelector} from "react-redux"
import {fetchBudgets, createBudget, fetchBudgetCategories, updatedBudgetCategoryName, deleteBudgetCategory, setActiveBudgetId, budgetMoney} from "./budgetsSlice"
import {selectAllCategories, fetchCategories, createCategory, editCategory, deleteCategory} from "../categories/categoriesSlice"
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
    const currentBudgetCategories = useSelector(state => state.budgets.budgetCategories)
    const budgetsStatus = useSelector(state => state.budgets.status)
    const budgetCategoriesStatus = useSelector(state => state.budgets.budgetCategoriesStatus)

    useEffect(() => {
        if (budgetId !== null) {
            dispatch(fetchBudgetCategories({
                budgetId: budgetId
            }))
            dispatch(setActiveBudgetId({
                budgetId: budgetId
            }))
        }
    }, [budgetId])
    
    
    useEffect(() => {
        if(budgetsStatus === 'idle') {
            dispatch(fetchBudgets())
        }
        
        if(budgetsStatus === 'succeeded' && budgetId === null) {
            if(currentMonthBudget === undefined) {
                    dispatch(createBudget({
                        month: new Date().getMonth() + 1,
                        year: new Date().getFullYear()
                    }))
            }
        }
    }, [budgetsStatus])

    useEffect(() => {
        if (budgetId === null && currentMonthBudget !== undefined) {
            setBudgetId(currentMonthBudget.id)
        }
    }, [currentMonthBudget])

    // Adding categories
    const [newCategoryFormVisible, setNewCategoryFormVisible] = useState(false);
    const handleCategoryCreate = async (values) => {
        try {
            await dispatch(createCategory(values)).unwrap()
            dispatch(fetchBudgetCategories({budgetId: budgetId}))
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

    // Editing existing categories
    const [editedCategory, setEditedCategory] = useState(null)
    const [editCategoryFormVisible, setEditCategoryFormVisible] = useState(false)
    const showEditCategoryDialog = async (category) => {
        setEditedCategory(category)
        setEditCategoryFormVisible(true)
    }
    const handleCategoryEdit = async (values) => {
        try {
            await dispatch(editCategory({
                categoryId: editedCategory.id,
                payload: values
            })).unwrap()
            dispatch(updatedBudgetCategoryName({
                categoryId: editedCategory.id,
                categoryName: values.name
            }))
        } catch (e) {
            message.error(`There was an error while editing the category: ${e.message}`, 10)
        } finally {
            setEditCategoryFormVisible(false)
        }
    }

    // Deleting categories
    const categoryDeleteStatus = useSelector(state => state.categories.deleteStatus)
    const handleCategoryDelete = async (budgetCategory) => {
        try {
            await dispatch(deleteCategory({
                categoryId: budgetCategory.category.id
            })).unwrap()
            dispatch(deleteBudgetCategory({
                categoryId: budgetCategory.category.id
            }))
        } catch (e) {
            message.error(`There was an error while deleting the ${budgetCategory.category.name} category: ${e.message}`, 10)
        }
    }

    // Budgeting money
    const [isBudgetingFormVisible, setIsBudgetingFormVisible] = useState(false)
    const [budgetingFormData, setBudgetingFormData] = useState(null)
    const showBudgetingDialog = (record) => {
        setBudgetingFormData(record)
        setIsBudgetingFormVisible(true)
    }
    const handleBudgeting = async (values) => {
        try {
            await dispatch(budgetMoney({
                budgetId: budgetId,
                categoryId: budgetingFormData.category.id,
                amount: values.amount * 100
            })).unwrap()
            await dispatch(fetchBudgets())
            await dispatch(fetchBudgetCategories({
                budgetId: budgetId
            }))
        } catch (e) {
            message.error(`There was an error while budgeting money for ${budgetingFormData.category.name} category: ${e.message}`, 10)
        } finally {
            setIsBudgetingFormVisible(false)
        }
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
                onCreate={handleCategoryEdit}
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
                loading={budgetCategoriesStatus !== 'succeeded'}
                pagination={false}
                size="small">
                <Column
                    title="Edit category"
                    key="edit-category"
                    width="5%"
                    render={(text, record) => {
                        return <EditTwoTone onClick={() => showEditCategoryDialog(record.category)}/>
                    }}/>
                <Column
                    title="Delete category"
                    key="delete-category"
                    width="5%"
                    render={(text, record) => {
                        return <Popconfirm
                            title="Are you sure to delete this category?"
                            okButtonProps = {{ loading: categoryDeleteStatus === 'loading' }}
                            onConfirm={() => handleCategoryDelete(record)}
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
                            <DollarTwoTone onClick={() => showBudgetingDialog(record)}/>
                        </Tooltip>
                    }}/>
            </Table>
            <BudgetingForm visible={isBudgetingFormVisible}
                           onCreate={(values) => handleBudgeting(values)}
                           onCancel={() => setIsBudgetingFormVisible(false)}
                           data={budgetingFormData}/>
        </div>
    )
}

export default BudgetsView