import HeatMap from '@uiw/react-heat-map';
import {Tabs, Tooltip, DatePicker, Button, Spin, Empty} from "antd";
import {useState, useEffect} from "react"
import {useSelector, useDispatch} from 'react-redux'
import {fetchHeatMaps, fetchExpensesByCategory} from "./reportsSlice"
import ReactApexChart from "react-apexcharts"

const {TabPane} = Tabs;
const {RangePicker} = DatePicker;

const getThisDayByYear = year => new Date(new Date().setFullYear(year))

const ReportsPeriodPicker = (props) => {

    const handleYearChange = (date, dateString) => {
        const year = parseInt(dateString)
        return (callback) => callback(new Date(year, 1, 1), new Date(year, 11, 31))
    }

    const handleLast12MonthsPick = () => {
        const endDate = new Date()
        const startDate = new Date(new Date().setFullYear(endDate.getFullYear() - 1))
        return (callback) => callback(startDate, endDate)
    }

    return (
        <div>

            <DatePicker picker="year" onChange={(x, y) => handleYearChange(x, y)((startDate, endDate) => {
                console.dir(props)
                console.log('will call callback in date pick')
                props.onDatePick(startDate, endDate)
            })}/>

            <Button type="primary" onClick={(x, y) => handleLast12MonthsPick(x, y)((startDate, endDate) => {
                props.onLast12MonthsPick(startDate, endDate)
            })}>
                Last 12 months
            </Button>
        </div>
    )
}

const ReportsView = () => {

    const dispatch = useDispatch()

    const [heatMapEndDate, setHeatMapEndDate] = useState(new Date())
    const [heatMapStartDate, setHeatMapStartDate] = useState(getThisDayByYear(new Date().getFullYear() - 1))
    const reportsStatus = useSelector(state => state.reports.status)
    const heatmapData = useSelector(state => {
        return {
            outcomes: state.reports.outcomesHeatMapData,
            incomes: state.reports.incomesHeatMapData
        }
    })
    useEffect(() => {
        console.log('foo')
        dispatch(fetchHeatMaps({
            startDate: heatMapStartDate.toISOString().split('T')[0],
            endDate: heatMapEndDate.toISOString().split('T')[0]
        }))
    }, [heatMapStartDate, heatMapEndDate])

    const [expensesByCategoryStartDate, setExpensesByCategoryStartDate] = useState(getThisDayByYear(new Date().getFullYear() - 1))
    const [expensesByCategoryEndDate, setExpensesByCategoryEndDate] = useState(new Date())
    const expensesByCategoryStatus = useSelector(state => state.reports.expensesByCategoryStatus)
    const expensesByCategoryData = useSelector(state => state.reports.expensesByCategoryData.map(o => o.sum / 100))
    const expensesByCategoryLabels = useSelector(state => state.reports.expensesByCategoryData.map(o => o.categoryName))
    useEffect(() => {
        dispatch(fetchExpensesByCategory({
            startDate: expensesByCategoryStartDate.toISOString().split('T')[0],
            endDate: expensesByCategoryEndDate.toISOString().split('T')[0]
        }))
    }, [expensesByCategoryStartDate, expensesByCategoryEndDate])

    const expensesByCategoryOptions = {
        chart: {
            width: 380,
            type: 'pie',
        },
        labels: expensesByCategoryLabels,
        responsive: [{
            breakpoint: 480,
            options: {
                chart: {
                    width: 200
                },
                legend: {
                    position: 'bottom'
                }
            }
        }]
    }

    const calculateFill = (value, max) => {
        const ratio = value / max * 100;

        if (100 - ratio < 25) {
            return 25
        }
        if (100 - ratio > 90) {
            return 90
        }
        return 100 - ratio;
    }

    const apiDataToHeatMapData = (apiData) => {
        return apiData.map(x => {
            const result = {
                date: x.date,
                count: x.sum
            }
            return result
        })
    }

    const maxSum = (arr) => {
        let max = arr[0].sum
        for (let i = 0; i < arr.length; i++) {
            if (arr[i].sum > max) {
                max = arr[i].sum
            }
        }
        return max
    }

    const handleHeatmapPeriodChange = (startDate, endDate) => {
        setHeatMapStartDate(startDate)
        setHeatMapEndDate(endDate)
    }

    const handleExpensesByCategoryPeriodChange = (dates, datesString) => {
        setExpensesByCategoryStartDate(new Date(datesString[0]))
        setExpensesByCategoryEndDate(new Date(datesString[1]))
    }
    return (<>
            <Tabs defaultActiveKey="1" size="large" style={{marginBottom: 32}}>
                <TabPane tab="Heat map" key="1">
                    <ReportsPeriodPicker
                        onDatePick={handleHeatmapPeriodChange}
                        onLast12MonthsPick={handleHeatmapPeriodChange}/>

                    <Spin spinning={reportsStatus === 'loading'}>
                        <HeatMap value={apiDataToHeatMapData(heatmapData.outcomes)} width="100%" rectSize={25}
                                 startDate={heatMapStartDate}
                                 endDate={heatMapEndDate} rectRender={(props, data) => {
                            return (
                                <Tooltip title={`${data.date}: $${(data.count || 0) / 100}`}>
                                    <rect {...props}
                                          fill={data.count === undefined ?
                                              '#ebedf0' :
                                              `hsl(2, 100%, ${calculateFill(data.count, maxSum(heatmapData.outcomes))}%)`}
                                    />
                                </Tooltip>
                            );
                        }} legendCellSize={0}/>


                        <HeatMap value={apiDataToHeatMapData(heatmapData.incomes)} width="53%" rectSize={14}
                                 startDate={heatMapStartDate}
                                 endDate={heatMapEndDate} rectRender={(props, data) => {
                            return (
                                <Tooltip title={`${data.date}: $${(data.count || 0) / 100}`}>
                                    <rect {...props}
                                          fill={data.count === undefined ?
                                              '#ebedf0' :
                                              `hsl(120, 100%, ${calculateFill(data.count, maxSum(heatmapData.incomes))}%)`}/>
                                </Tooltip>
                            );
                        }} legendCellSize={0}/>
                    </Spin>
                </TabPane>

                <TabPane tab="Expenses by category" key="2">
                    <RangePicker
                        onChange={handleExpensesByCategoryPeriodChange}/>
                    <Button type="primary">Show this month</Button>
                    <Spin spinning={expensesByCategoryStatus === 'loading'}>
                        {expensesByCategoryData.length === 0 ? <Empty/> :
                            <ReactApexChart options={expensesByCategoryOptions}
                                            series={expensesByCategoryData}
                                            type="pie" width="40%"/>
                        }
                    </Spin>
                </TabPane>
            </Tabs>
        </>
    )
}

export default ReportsView;