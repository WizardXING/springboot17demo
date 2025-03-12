// 初始化地图
function initMap() {
    const mapChart = echarts.init(document.getElementById('china-map'));
    
    // 加载位置数据
    fetch('/api/locations/grouped-by-city')
        .then(response => response.json())
        .then(data => {
            const cities = Object.keys(data);
            const locationData = [];
            
            // 处理每个城市的数据
            cities.forEach((city, index) => {
                const locations = data[city];
                if (locations && locations.length > 0) {
                    // 使用第一个位置的经纬度作为城市坐标
                    locationData.push({
                        name: city,
                        value: [locations[0].longitude, locations[0].latitude],
                        locations: locations,
                        itemStyle: {
                            color: getRandomColor(index)
                        }
                    });
                }
            });
            
            // 配置地图选项
            const option = {
                title: {
                    text: '我的足迹地图',
                    left: 'center'
                },
                tooltip: {
                    trigger: 'item',
                    formatter: function(params) {
                        if (!params.data || !params.data.locations) return params.name;
                        
                        const locations = params.data.locations;
                        let html = `<div style=\"font-size:14px;color:#666;\">${params.name}</div>`;
                        html += '<ul style=\"padding-left:20px;margin:5px 0;\">';
                        
                        locations.forEach(loc => {
                            html += `<li>${loc.visitDate}: ${loc.locationName}</li>`;
                            if (loc.notes) {
                                html += `<li style=\"color:#999;font-size:12px;\">${loc.notes}</li>`;
                            }
                        });
                        
                        html += '</ul>';
                        return html;
                    }
                },
                geo: {
                    map: 'china',
                    roam: true,
                    label: {
                        show: true,
                        fontSize: 12,
                        color: '#333'
                    },
                    itemStyle: {
                        areaColor: '#f3f3f3',
                        borderColor: '#ddd'
                    },
                    emphasis: {
                        itemStyle: {
                            areaColor: '#e6e6e6'
                        },
                        label: {
                            color: '#000'
                        }
                    }
                },
                series: [{
                    name: '城市',
                    type: 'scatter',
                    coordinateSystem: 'geo',
                    data: locationData,
                    symbolSize: 12,
                    label: {
                        show: false
                    },
                    emphasis: {
                        label: {
                            show: true
                        }
                    }
                }]
            };
            
            mapChart.setOption(option);
        })
        .catch(error => console.error('加载位置数据失败:', error));
}

// 生成随机颜色
function getRandomColor(index) {
    const colors = [
        '#ff7f50', '#87cefa', '#da70d6', '#32cd32', '#6495ed',
        '#ff69b4', '#ba55d3', '#cd5c5c', '#ffa500', '#40e0d0'
    ];
    return colors[index % colors.length];
}

// 页面加载完成后初始化地图
document.addEventListener('DOMContentLoaded', initMap);