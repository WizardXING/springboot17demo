function sendMessage() {
    const message = document.getElementById('messageInput').value;
    if (!message) {
        alert('请输入消息内容');
        return;
    }

    fetch('/api/rocketmq/send', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ message: message })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('消息发送成功');
            document.getElementById('messageInput').value = '';
        } else {
            alert('消息发送失败：' + data.message);
        }
    })
    .catch(error => {
        console.error('消息发送失败:', error);
        alert('消息发送失败，请查看控制台了解详细信息。');
    });
}

function sendDelayMessage() {
    const message = document.getElementById('messageInput').value;
    const delayLevel = document.getElementById('delayLevel').value;

    if (!message) {
        alert('请输入消息内容');
        return;
    }

    if (!delayLevel || isNaN(delayLevel) || delayLevel < 1 || delayLevel > 18) {
        alert('请输入有效的延迟级别（1-18）');
        return;
    }

    fetch('/api/rocketmq/sendDelay', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            message: message,
            delayLevel: parseInt(delayLevel)
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('延迟消息发送成功');
            document.getElementById('messageInput').value = '';
            document.getElementById('delayLevel').value = '';
        } else {
            alert('延迟消息发送失败：' + data.message);
        }
    })
    .catch(error => {
        console.error('延迟消息发送失败:', error);
        alert('延迟消息发送失败，请查看控制台了解详细信息。');
    });
}

function initRocketMQ() {
    fetch('/api/rocketmq/init', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .catch(error => {
        console.error('RocketMQ初始化失败:', error);
        alert('RocketMQ初始化失败，请查看控制台了解详细信息。');
    });
}
