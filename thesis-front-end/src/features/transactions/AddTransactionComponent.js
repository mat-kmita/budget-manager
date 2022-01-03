import React, {useState} from "react";
import {useDispatch} from "react-redux"
import {useParams} from "react-router-dom"
import {Button, Modal, Form, Input, InputNumber, DatePicker, message} from "antd";
import "antd/dist/antd.css";

import {addNewTransaction} from "./transactionsSlice"
import {fetchAccounts} from "../accounts/accountsSlice"

import CategoriesSelect from "../categories/CategoriesSelect"

const TransactionAddForm = ({visible, onCreate, onCancel}) => {
    const [form] = Form.useForm();
    return (
        <Modal
            visible={visible}
            title="Create a new transaction"
            okText="Create"
            cancelText="Cancel"
            onCancel={onCancel}
            onOk={() => {
                form
                    .validateFields()
                    .then((values) => {
                        form.resetFields();
                        values.categoryId = values.categoryId.categoryId
                        console.dir(values)
                        onCreate(values);
                    })
                    .catch((info) => {
                        console.log('Validate Failed:', info);
                    });
            }}>
            <Form
                form={form}>
                <Form.Item
                    name="date"
                    label="Date"
                    rules={[
                        {
                            required: true,
                            message: 'Please input the date of the transaction!',
                        },
                    ]}>
                    <DatePicker showToday={true} formet="dd-MM-YYYY"/>
                </Form.Item>
                <Form.Item
                    name="memo"
                    label="Memo">
                    <Input/>
                </Form.Item>
                <Form.Item
                    name="amount"
                    label="Amount (positive for income, negative for expense)"
                    rules={[
                        {
                            required: true,
                            message: 'Please input the amount of the transaction!'
                        }
                    ]}>

                    <InputNumber/>
                </Form.Item>
                <Form.Item
                    name="payee"
                    label="payee"
                    rules={[
                        {
                            required: true,
                            message: 'Please input the payee of the transaction!'
                        }
                    ]}>

                    <Input/>
                </Form.Item>
                <Form.Item
                    name="categoryId"
                    label="category"
                    rules={[
                        {
                            required: true,
                            message: 'Please input the category of the transaction!'
                        }
                    ]}>

                    <CategoriesSelect />
                </Form.Item>
            </Form>
        </Modal>
    );
};

const AddTransactionComponent = () => {
    const [visible, setVisible] = useState(false);

    const success = (id) => {
        message.success(`Succesfully created transaction with ID ${id}`)
    }

    const error = (err) => {
        message.error(`An error occured whil creating transaction: ${err}`)
    }

    const onCreate = async (values) => {
        try {
            await dispatch(addNewTransaction({payload: values, accountId: id})).unwrap()
            await dispatch(fetchAccounts()).unwrap()
            success(1)
        } catch (err) {
            error(err.message)
        } finally {
            setVisible(false);
        }

    };

    const {id} = useParams();

    const dispatch = useDispatch()

    return (
        <div>
            <Button
                type="primary"
                onClick={() => {
                    setVisible(true);
                }}>
                Add transaction
            </Button>
            <TransactionAddForm
                visible={visible}
                onCreate={onCreate}
                onCancel={() => {
                    setVisible(false);
                }}/>
        </div>
    );
};

export default AddTransactionComponent;