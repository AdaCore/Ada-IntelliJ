#!/bin/bash

if [[ ${#} -ne 2 ]]; then
	echo 'Usage: ./deploy.sh <deploy-token> <file-path>'
	exit 1
fi

token=${1}
filePath=${2}

if [ ! -f "${filePath}" ]; then
	echo "File not found: ${filePath}"
	exit 1
fi

pluginXmlId='com.adacore.Ada-IntelliJ'

curl -i --header "Authorization: Bearer ${token}" \
	-F xmlId=${pluginXmlId} \
	-F file=@${filePath} \
	https://plugins.jetbrains.com/plugin/uploadPlugin
