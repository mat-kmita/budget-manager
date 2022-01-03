import React from "react-dom"
import {Modal, Form, Input, InputNumber, Select} from 'antd';
import 'antd/dist/antd.css';

const EditForm = ({visible, onCreate, onCancel, transaction}) => {
    const [form] = Form.useForm();

    return (
        <Modal
            visible={visible}
            title="Add new account"
            okText="Add"
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
            <Form form={form}>
                <Form.Item
                    label="Name"
                    name="name"
                    rules={[
                        {
                            required: true,
                            message: 'Please input account name'
                        }
                    ]}>
                    <Input/>
                </Form.Item>
                <Form.Item
                    label="Description"
                    name="description">
                    <Input/>
                </Form.Item>
                <Form.Item
                    label="Initial balance"
                    name="initialBalance">
                    <InputNumber precision={2}/>
                </Form.Item>
                <Form.Item
                    label="Account type"
                    name="accountType"
                    rules={[
                        {
                            required: true,
                            message: "Please select account type"
                        }
                    ]}>

                    <Select
                        placeholder="Select account type"
                        options={[
                            {
                                value: "CHECKING",
                                label: "Checking"
                            },
                            {
                                value: "SAVING",
                                label: "Saving"
                            },
                            {
                                value: "CASH",
                                label: "Cash"
                            },
                            {
                                value: "OTHER",
                                label: "Other"
                            }
                        ]}/>
                </Form.Item>
            </Form>
        </Modal>
    )
}

const NewAccountForm = ({visible, handleCancel, handleCreate}) => {
    const onCreate = (values) => {
        handleCreate(values)
    };

    return (
        <div>
            <EditForm
                visible={visible}
                onCreate={onCreate}
                onCancel={handleCancel}/>
        </div>
    );
};

export default NewAccountForm; 