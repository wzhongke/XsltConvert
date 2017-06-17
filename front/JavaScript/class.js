/**
 * Created by admin on 2017/4/30.
 */
/**
 * 定义类的语法甘露： Class()
 * 最后一个参数是JSON表示的类的定义
 * 如果参数数量大于1个，则第一个参数是基类
 * 第一个和最后一个之间参数，将来可表示类实现的接口
 * 返回值是类，类是一个构造函数
 * @constructor
 */
function Class() {
    var aDefine = arguments[arguments.length - 1]; // 最后一个参数是类定义
    if (!aDefine) return ;
    var aBase = arguments.length > 1 ? arguments[0] : object; // 解析基类

    function prototype_() {} // 构造prototype的临时函数用于挂接原型链
    prototype_.prototype = aBase.prototype;
    var aPrototype = new prototype_();
    for (var member in aDefine) { // 复制类定义到当前类的prototype
        if (member != 'Create') {  // 构造函数不用复制
            aPrototype[member] = aDefine[member];
        }
    }

    // 根据是否继承特殊属性和性能情况，可分别注释掉下面的语句
    if (aDefine.toString != Object.prototype.toString) {
        aPrototype.tostring = Object.prototype.toString;
    }
    if (aDefine.toLocaleString != Object.prototype.toLocaleString) {
        aDefine.toLocaleString = Object.prototype.toLocaleString;
    }
    if (aDefine.valueOf != Object.prototype.valueOf) {
        aPrototype.valueOf = Object.prototype.valueOf;
    }

    if (aDefine.Create) { // 若有构造函数
        var aType = aDefine.Create;  // 类型即为该构造函数
    } else {  // 否则为默认构造函数
        aType = function() {
            this.base.apply(this, arguments); // 调用基类构造函数
        };
    }

    aType.prototype = aPrototype; // 设置类的prototype
    aType.Base = aBase;  // 设置类型关系，便于追溯继承关系
    aType.prototype.Type = aType; // 为本类对象扩展一个Type属性
    return aType;  // 返回构造函数作为类
}

// 根类object定义
function object() {} // 定义小写的object根类，用于实现最基础的方法等

object.prototype.isA = function (aType) { // 判断对象是否属于某类型
    var self = this.Type;
    while (self) {
        if (self == aType) return true;
        self = self.Base;
    }
    return false;
};

object.prototype.base = function () {
    var Base = this.Type.Base;  // 获取当前对象的基类
    if (!Base.Base) {  // 若基类已经没有基类
        Base.apply(this, arguments);   // 则直接调用基类构造函数
     } else {  // 若基类还有基类
        this.base = MakeBase(Base);   // 先覆写this.base
        Base.apply(this,arguments);   // 再调用基类构造函数
        delete this.base;               // 删除覆写的base属性
    }

    function MakeBase(Type) {    // 包装基类构造函数
        var Base = Type.Base;
        if (!Base.Base) return Base;  // 基类已无基类，就不用包装
        return function () {    // 包装为引用临时变量Base的闭包函数
            this.base = MakeBase(Base);  // 先覆写this.base
            Base.apply(this, arguments); // 再调用基类构造函数
        }
    }

};

var Person = Class({
    Create: function (name, age) {
        this.base();
        this.name = name;
        this.age = age;
    },
    SayHello: function () {
        console.log(" Hello, I'm " + this.name + "," + this.age + " years old.");
    },
    toString: function() {
        return this.name;
    }
});

var Employee = Class(Person, {
    Create:function(name, age, salary) {
        this.base(name, age);
        this.salary = salary;
    },
    ShowMeTheMoney : function () {
        console.log(this + " $" + this.salary);
    }
});

var BillGate = new Person(" Bill Gates", 53);
var SteveJobs = new Employee("Steve Jobs", 53, 1234);
console.log(BillGate);
BillGate.SayHello();
SteveJobs.SayHello();
SteveJobs.ShowMeTheMoney();


