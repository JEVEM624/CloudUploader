# CloudUploader

## 简介

作为一个工具，支持多个公司云服务，方便开发者将文件上传到对象存储中

## 支持

- [x] 七牛[(https://www.qiniu.com/)](https://www.qiniu.com/) 
- [X] 阿里云[(https://www.aliyun.com/)](https://www.aliyun.com/)

## comming soon

- [ ] 腾讯云
- [ ] 又拍云

## 使用方法

1.在properties文件中进行相关配置


```properties

tmp= #大文件需要分割，请指定文件夹地址
retrytimes=3#重试次数
thread.nums=10#每个云的上传核心线程数量，线程最大数量为两倍
cloud= #需要使用云，使用逗号分隔

```

七牛云配置

```properties

qiniu.region= #七牛云地区
qiniu.AK= #七牛云 AccessKey
qiniu.SK= #七牛云 SecretKey
qiniu.bucket= #七牛云存储空间名

```

阿里云配置

```properties

aliyun.AK=  #阿里云 AccessKey
aliyun.SK=  #阿里云SecretKet
aliyun.bucket=  #阿里云OSS Bucket名
aliyun.ossDomain= #阿里云 Bucket 域名

```
2.在需要使用的地方获取CloudUploader对象

```java

CloudUploader cloudUploader = CloudUploader.getInstance();

```

3.使用put方法提交文件的path即可

```java

cloudUploader.put(path);

```

License MIT