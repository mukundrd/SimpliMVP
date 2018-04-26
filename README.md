# SimpliMVP
SimpliMVP is an effort to create a simple and understandable MVP Library. Idea here is to create a basic code structure 
that can be further extend based on the project needs.

# Why SimpliMVP?
There are already lots of good article and library available on MVP, then why you need SimpliMVP?
My simple answer to this question is, thou there are lots of good material available. I personally find, few libraries are little complicated and require lot of code to put in to manage that.
Therefore, I thought of creating foundation of MVP, that can be further extend.

# How to use?
1. To use the SimpliMVP architecture, your Activity/Fragment must extend SimpliActivity/SimpliFragment respectively along with mentioning the class as ```@SimpliViewComponent``` with the help of annotation.

2. Presenters must extend ```SimpliPresenter``` and view contract interfaces must extend ```SimpliView```.

3. The dependency injection and instantiation of your presenter will be taken care by SimpliMVP library with the help of ```simpliMVPAnnotationProcessor``` annotation processor.

# How to add dependencies?
Copy the dependency as

```
dependencies {
    ...
    
    implementation 'com.trayis:simpliMVP:1.1.0'

    implementation "com.trayis:simpliMVPAnnotationProcessor:0.2.1"
    annotationProcessor "com.trayis:simpliMVPAnnotationProcessor:0.2.1"
    
    ...
}
```
