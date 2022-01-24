import {useEffect} from "react"
import React from "react-dom"
import {Modal, Form, Input} from "antd";
import "antd/dist/antd.css";

const EditCategoryForm = ({visible, onCreate, onCancel, category}) => {
    const [form] = Form.useForm();

    useEffect(() => {
        form.setFieldsValue({
            name: category?.name
        })
    }, [visible])

    return (
        <Modal
            visible={visible}
            title="Edit category"
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
                            id: category.id
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
                    name="name">
                    <Input/>
                </Form.Item>
            </Form>
        </Modal>
    )
}

export default EditCategoryForm;
