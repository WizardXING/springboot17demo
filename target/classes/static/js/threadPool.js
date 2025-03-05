function checkThreadPoolStatus() {
    fetch('/api/thread-pool/status')
        .then(response => response.json())
        .then(data => {
            const message = `线程池状态：\n\n` +
                          `活跃线程数：${data.activeCount}\n` +
                          `核心线程数：${data.corePoolSize}\n` +
                          `最大线程数：${data.maxPoolSize}\n` +
                          `当前线程池大小：${data.poolSize}\n` +
                          `队列中等待的任务数：${data.queueSize}`;
            alert(message);
        })
        .catch(error => {
            console.error('获取线程池状态失败:', error);
            alert('获取线程池状态失败，请查看控制台了解详细信息。');
        });
}