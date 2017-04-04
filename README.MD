# Scala Quant Lib

This project is trying to provide an native Scala adaptation of the popular C++ QuantLib project.

While trying to maintain the same function and concept of the original project, ScalaQuantLib will try to incorporate conceptions and practices with more modern approach. Such changes will include but not limited to Pattern matching, Actor models(Akka), Immutable date/time and etc.

This project is still in early infancy and it is still a personal side project without any funding and dedication of time. There are lots of bugs, missing documents and features, even missing test cases.

The current layout of the project is separated into sub projects with binary dependencies. Each projected is being working indecently

### quantlib-temporal
> Provide utility functions to all thing related to business calendars, schedule, day count basis 

### quantlib-numeric
> Provide basic calculation functions and mathematical models such as distributions, capulars, integrals and other related. 

### quantlib-financial:
> Provide basic financial instrument abstraction, such as Stock, Bond, Swap and other derivatives.

### quantlib-quantitative:
> Provide the actual quantitative analytical tooling such as pricing model/engine and related.

### quantlib-http:
> Using akka-http as the http toolkit to provide user interaction


## License
--------

This project is released under the same [license](http://quantlib.org/license.shtml) as quantlib.

