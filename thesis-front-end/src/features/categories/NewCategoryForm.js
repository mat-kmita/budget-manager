import React from "react-dom"
import {Modal, Form, Input} from "antd";
import "antd/dist/antd.css";

const NewCategoryForm = ({visible, onCreate, onCancel}) => {
    const [form] = Form.useForm();

    return (
        <Modal
            visible={visible}
            title="Create new category"
            okText="Create"
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
                        onCreate({
                            ...values,
                        });
                    })
                    .catch((info) => {
                        console.log('Validate Failed:', info);
                    });
            }}>
            <Form
                form={form}>
                <Form.Item
                    label="Name"
                    name="name"
                    rules={[
                        {
                            required: true,
                            message: 'Please input the category of the transaction!'
                        }
                    ]}>
                    <Input/>
                </Form.Item>
            </Form>
        </Modal>
    )
}

export default NewCategoryForm;