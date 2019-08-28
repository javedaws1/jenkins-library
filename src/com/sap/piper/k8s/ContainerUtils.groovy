package com.sap.piper.k8s

class ContainerUtils {


    static waitForSidecarReadyOnDocker(String containerId, String command){
        String dockerCommand = "docker exec ${containerId} ${command}"
        waitForSidecarReady(dockerCommand)
    }

    static waitForSidecarReadyOnKubernetes(String containerName, String command){
        container(name: containerName){
            waitForSidecarReady(command)
        }
    }

    static waitForSidecarReady(String command){
        int sleepTimeInSeconds = 10
        int timeoutInSeconds = 5 * 60
        int maxRetries = timeoutInSeconds / sleepTimeInSeconds
        int retries = 0
        while(true){
            echo "Waiting for sidecar container"
            String status = sh script:command, returnStatus:true
            if(status == "0") return
            if(retries > maxRetries){
                error("Timeout while waiting for sidecar container to be ready")
            }

            sleep sleepTimeInSeconds
            retries++
        }
    }
}
