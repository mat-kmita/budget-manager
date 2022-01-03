import {useEffect} from "react"
import React from "react-dom"
import {Modal, Form, Input, Select} from 'antd';
import 'antd/dist/antd.css';

const EditForm = ({visible, onCreate, onCancel, account}) => {
    const [form] = Form.useForm();

    useEffect(() => {
        form.setFieldsValue({
            name: account?.name,
            description: account?.description,
            accountType: account?.accountType
        })
    }, [visible])
    
    return (
        <Modal
            visible={visible}
            title="Edit account"
            okText="Save"
            cancelText="Cancel"
            onCancel={ () => {
                form.resetFields();
                onCancel();
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
                    label="Name"
                    name="name">
                    <Input value={account?.name}/>
                </Form.Item>
                <Form.Item
                    label="Description"
                    name="description">
                    <Input/>
                </Form.Item>
                <Form.Item
                    label="Account type"
                    name="accountType">
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

const AccountEditForm = ({visible, handleCancel, handleCreate, account}) => {
    const onCreate = (values) => {
        handleCreate(values)
    }

    return (
        <div>
            <EditForm
                visible={visible}
                onCreate={onCreate}
                onCancel={handleCancel}
                account={account}/>
        </div>
    );
};

export default AccountEditForm;
