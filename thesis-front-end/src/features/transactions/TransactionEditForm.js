import {useEffect} from "react"
import React from "react-dom"
import {Modal, Form, Input, InputNumber, DatePicker} from "antd";
import "antd/dist/antd.css";
import moment from "moment"
import CategoriesSelect from "../categories/CategoriesSelect"

const TransactionsEditForm = ({visible, onCreate, onCancel, transaction}) => {
    const [form] = Form.useForm();

    useEffect(() => {
        form.setFieldsValue({
            date: moment(transaction?.date, "YYYY-MM-DD"),
            payee: transaction?.payee,
            memo: transaction?.memo,
            amount: transaction?.amount / 100,
        })
    }, [visible])

    return (
        <Modal
            visible={visible}
            title="Edit transaction"
            okText="Save"
            cancelText="Cancel"
            onCancel={() => {
                form.resetFields();
                onCancel();
            }}
            onOk={() => {
                form
                    .validateFields()
                    .then((values) => {
                        form.resetFields();
                        values.date = values.date.toString()
                        onCreate({
                            ...values,
                            id: transaction.id,
                            categoryId: values.categoryId?.categoryId
                        });
                    })
                    .catch((info) => {
                        console.log('Validate Failed:', info);
                    });
            }}>
            <Form
                form={form}>
                <Form.Item
                    label="Date"
                    name="date">
                    <DatePicker showToday={true} format="DD-MM-YYYY"/>
                </Form.Item>
                <Form.Item
                    label="Payee"
                    name="payee">
                    <Input/>
                </Form.Item>
                <Form.Item
                    label="Memo"
                    name="memo">
                    <Input/>
                </Form.Item>
                <Form.Item
                    label="Amount (positive for income, negative for expense)"
                    name="amount">
                    <InputNumber precision={2}/>
                </Form.Item> 
                <Form.Item
                    label="Category"
                    name="categoryId">

                    <CategoriesSelect/>
                </Form.Item>
            </Form>
        </Modal>
    )
}

export default TransactionsEditForm;