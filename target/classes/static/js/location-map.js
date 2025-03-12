let mapChart;
let locationDataLoaded = false;

function initMap() {
    mapChart = echarts.init(document.getElementById('china-map'));
    
    // 初始化地图配置
    const baseOption = {
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
            data: [],
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
    
    // 立即显示地图
    mapChart.setOption(baseOption);
    
    // 添加点击事件监听器
    mapChart.getZr().on('click', function() {
        if (!locationDataLoaded) {
            loadLocationData();
        }
    });
    
    // 首次加载数据
    loadLocationData();
}

function loadLocationData() {
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), 2000);
    
    fetch('/api/locations/grouped-by-city', {
        signal: controller.signal
    })
        .then(response => response.json())
        .then(data => {
            clearTimeout(timeoutId);
            const cities = Object.keys(data);
            const locationData = [];
            
            cities.forEach((city, index) => {
                const locations = data[city];
                if (locations && locations.length > 0) {
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
            
            mapChart.setOption({
                series: [{
                    data: locationData
                }]
            });
            
            locationDataLoaded = true;
        })
        .catch(error => {
            if (error.name === 'AbortError') {
                console.log('数据加载超时，点击地图重试');
            } else {
                console.error('加载位置数据失败:', error);
            }
            locationDataLoaded = false;
        });
}

function getRandomColor(index) {
    const colors = [
        '#1f77b4', '#ff7f0e', '#2ca02c', '#d62728', '#9467bd',
        '#8c564b', '#e377c2', '#7f7f7f', '#bcbd22', '#17becf'
    ];
    return colors[index % colors.length];
}

// 页面加载完成后初始化地图
document.addEventListener('DOMContentLoaded', initMap);