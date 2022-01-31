import React from "react-dom";
import {useEffect, useState} from "react";
import {useSelector, useDispatch} from 'react-redux'
import {Select} from "antd"
import {selectAllCategories, fetchCategories} from "./categoriesSlice"

export default function CategoriesSelect({ value = {}, onChange }) {
    const dispatch = useDispatch()  
    const categories = useSelector(selectAllCategories)

    const [categoryId, setCategoryId] = useState(null)

    const categoryStatus = useSelector(state => state.categories.status)
    const loading = () => categoryStatus === 'loading'

    useEffect(() => {
        if (categoryStatus === 'idle') {
            dispatch(fetchCategories())
        }
    }, [categoryStatus, dispatch])

    const triggerChange = (changedValue) => {
        onChange?.({
            categoryId,
            ...value,
            ...changedValue
        })
    }

    const onCategoryChange = (newCategoryId) => {
        if(!('categoryId' in value)) {
            setCategoryId(newCategoryId)
        }

        triggerChange({
            categoryId: newCategoryId
        })
    }

    return (
        <Select
            showSearch
            options={categories.map(category => {
                return {
                    label: category.name,
                    value: category.id
                }
            })}
            optionFilterProp="children"
            placeholder="Select a category"
            filterOption={(input, option) =>
                option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0
            }
            onChange={onCategoryChange}
            value={value?.categoryId || categoryId}>
        </Select>
    )
}