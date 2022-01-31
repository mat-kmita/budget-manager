import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux"
import {useParams} from "react-router-dom"
import {Button, Modal, Form, Input, InputNumber, DatePicker, Select, message} from "antd";
import "antd/dist/antd.css";
import moment from "moment"

const {Option} = Select;

const EditTransferForm = ({visible, onCreate, onCancel, transfer}) => {

    const {id} = useParams()

    const accounts = useSelector(state => state.accounts.accounts)
    
    useEffect(() => {
        form.setFieldsValue({
            date: moment(transfer?.date, "YYYY-MM-DD"),
            memo: transfer?.memo,
            amount: transfer?.amount / 100
        })

    }, [visible])

    const [form] = Form.useForm();
    return (
        <Modal
            visible={visible}
            title="Edit transfer"
            okText="Edit"
            cancelText="Cancel"
            onCancel={() => {
                form.resetFields()
                onCancel()
            }}
            onOk={() => {
                form
                    .validateFields()
                    .then((values) => {
                        form.resetFields();
                        onCreate(values);
                    })
                    .catch((info) => {
                        console.log('Validate Failed:', info);
                    });
            }}>
            <Form
                hasFeedback
                onFinish={e => e.preventDefault()}
                form={form}>
                <Form.Item
                    name="date"
                    label="Date"
                    rules={[
                        {
                            required: true,
                            message: 'Please input the date of the transfer!',
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
                    label="Amount"
                    rules={[
                        {
                            required: true,
                            message: 'Please input the amount of the transaction!'
                        },
                        {
                            validator: async (_, value) => {
                                value === 0 ? Promise.reject(new Error('Must not be 0!')) : Promise.resolve()
                            },
                            message: 'Amount mustnot not be 0!'
                        }
                    ]}>

                    <InputNumber precision={2}/>
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default EditTransferForm;
