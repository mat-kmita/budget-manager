import React, {useState} from "react";
import {useDispatch, useSelector} from "react-redux"
import {useParams} from "react-router-dom"
import {Button, Modal, Form, Input, InputNumber, DatePicker, Select, message} from "antd";
import "antd/dist/antd.css";

const {Option} = Select;

const MakeTransferForm = ({visible, onCreate, onCancel}) => {

    const accounts = useSelector(state => state.accounts.accounts)
    const {id} = useParams()

    const [form] = Form.useForm();
    return (
        <Modal
            visible={visible}
            title="Make a transfer"
            okText="Create"
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
                        }
                    ]}>

                    <InputNumber precision={2} min={0.01}/>
                </Form.Item>
                <Form.Item
                    name="toAccount"
                    label="Destination account"
                    rules={[
                        {
                            required: true,
                            message: 'Please input the account to transfer money to!'
                        }
                    ]}>

                    <Select>
                        {
                            accounts
                                .filter(account => account.id != id)
                                .map(account => <Option value={account.id}>{account.name}</Option>)
                        }
                    </Select>
                </Form.Item>
            </Form>
        </Modal>
    );
};

const AddTransactionComponent = () => {
    const dispatch = useDispatch()
    
    const {id} = useParams();
    
    const [visible, setVisible] = useState(false);

    const success = () => {
        message.success(`Succesfully created transfer`)
    }
    const error = (err) => {
        message.error(`An error occured whil creating transfer: ${err}`)
    }
    
    const onCreate = async (values) => {
        try {
            success()
        } catch (err) {
            error(err.message)
        } finally {
            setVisible(false)
        }
    };

    return (
        <div>
            <Button
                type="primary"
                onClick={() => {
                    setVisible(true);
                }}>
                Make a transfer  
            </Button>
            <MakeTransferForm
                visible={visible}
                onCreate={onCreate}
                onCancel={() => {
                    setVisible(false);
                }}/>
        </div>
    );
};

export default AddTransactionComponent;
