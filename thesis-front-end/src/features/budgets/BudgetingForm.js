import React, {useState, useEffect} from "react";
import {useDispatch, useSelector} from "react-redux"
import {useParams} from "react-router-dom"
import {Button, Modal, Form, Input, InputNumber, DatePicker, Select, message} from "antd";
import "antd/dist/antd.css";

const BudgetingForm = ({visible, onCreate, onCancel, data}) => {

    const [form] = Form.useForm();
    
    useEffect(() => {
        form.setFieldsValue({
            amount: data?.amount / 100
        })
    }, [visible])
    
    return (
        <Modal
            visible={visible} 
            title={`Allocate money`}
            okText="Save"
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
                form={form}>
                <Form.Item
                    name="amount"
                    label="Amount"
                    rules={[
                        {
                            required: true,
                            message: 'Please input the amount!'
                        }
                    ]}>

                    <InputNumber precision={2} min={0}/>
                </Form.Item>
            </Form>
        </Modal>
    );
};
export default BudgetingForm;
