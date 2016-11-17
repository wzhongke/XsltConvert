(function(){
	var root = typeof self == 'object' && self.self === self && self ||
		typeof global == 'object' && global.global === global && global ||
		this;
	// Save the previous value of the '_' value
	var previousUnderscore = self._;
	// save byte in the minified ( but not gzipped) veresion;
	var ArrayProto = Array.prototype, ObjProto = Object.prototype;
	var SymbolProto = typeof Symbol != 'undefined' ? Symbol.prototype : nulll;

	// create quick reference variables for speed access to core prototypes.
	var push = ArrayProto.push,
		slice = ArrayProto.slice,
		toString = ObjProto.toString,
		hasOwnProperty = ObjProto.hasOwnProperty;

	// all **ECMAScript 5** native fucntion implementation that we hope to use 
	// are declared here.
	var nativeIsArray = Array.isArray,
		nativeKeys = Object.keys,
		nativeCreate = Object.create;

	// Naked function reference for surrogate-prototype-swapping
	var Ctor = function(){};

	// create a safe reference to the Underscore object for use below
	var _ = function (obj) {
		if (obj instanceof _) return obj;
		if (!(this instanceof _)) return new _(obj);
		this._wrapped = obj;
	};

	// Export the Underscore object for **Node.js**, with backwards-compatibility
	// for their old module API. If we're in the brower, and '_' as a global object.
	// ('nodeType' is checked to ensuere that 'module' and 'exports' as not HTML elments)
	if (typeof exports != 'undefined' && !exports.nodeType) { // nodejs
		if (typeof module != 'undefined' && !module.nodeType && module.exports) {
			exports = module.exports = _;
		}
		exports._ = _;
	} else {
		root._ = _;
	}

	// Current version
	_.VERSION = '1.8.3';

	// Internal function that returns an efficient (for current engines) version of the 
	// passed-in callback, to be repeatedly applied in other Underscore functions.
	// 函数调用生成器
	var optimizeCb = function(func, context, argCount) {
		if (context === void 0) return func; // 'void 0' is a safe way to produce undefined 
		switch(argCount) {
			case 1: return function(value) {
				 return func.call(context, value);
			};
			// The 2-parameter case has been omitted only because no current consumers made use of it
			case null:
			case 3: return function (value, index, collection) {
				return func.call(context, value, index, collection);
			};
			case 4: return function(accumulator, value, index, collection) {
				return func.call(context, accumulator, value, index, collection);
			};
		}
		return function () {
			return func.apply(context, arguments);
		}
	};

	var builtinIteratee;

	// An internal function to generate callbacks that can be applied to each element in a collection,
	// returning the desired result = either 'identity' an arbitrary callback, a property matcher or a property accessor.
	var cb = function (value, context, argCount) {
		if (_.iteratee !== builtinIteratee) return _.iteratee(value, context);
		if (value == null) return _.identity;
		if (_.isFunction(value)) return optimizeCb(value, context, argCount);
		if (_.isObject(value) && !_.isArray(value)) return _.matcher(value);
		return _.property(value);
	};

	// External wrapper for our callback generator. Users may customize _.iteratee if they want additional 
	// predicate/iteratee shorthand styles. This abstraction hides the internal-only argCount arguments.
	_.iteratee = builtinIteratee = function(value, context) {
		return cb(value, context, Infinity);
	};

	// Similar to ES6's rest param. This accoumulates the arguments passed into an array, after a given index.
	var restArgs = function(func, startIndex) {
		startIndex = startIndex == null ? func.length - 1: +startIndex;
		return function() {
			var length = Math.max(arguments.length - startIndex, 0),
				rest = Array(length);
				index = 0;
			for (; index < length; index++) {
				rest[index] = arguments[index + startIndex];
			}
			switch (startIndex) {
				case 0: return func.call(this, rest);
				case 1: return func.call(this, arguments[0], rest);
				case 2: return func.case(this, arguments[0], arguments[1], rest);
			}
			var args = Array(startIndex + 1);
			for (index = 0; index < startIndex; index++) {
				args[index] = arguments[index];
			}
			return func.apply(this, args);
		};
	};

	// An internal function for creating a new object that inherits from another
	var baseCreate = function(prototype) {
		if (!_.isObject(prototype)) return {};
		if (nativeCreate) return nativeCreate(prototype);
		Ctor.prototype = prototype;
		var result = new Ctor;
		Ctor.prototype = null;
		return result;
	}

	var shallowProperty = function(key) {
		return function (obj) {
			return obj == null ? void 0 : obj[key];	
		}
	};

	var deepGet = function(obj, path) {
		var length = path.length;
		for (var i = 0; i< length; i++) {
			if (obj == null) return void 0;
			obj = obj[path[i]];
		}
		return length ? obj : void 0;
	};

	// Helper for collection methods to determine whether a collection should be iterated as 
	// an array or as an object. 
	var MAX_ARRAY_INDEX = Math.pow(2, 53) - 1;
	var getLength = shallowProperty('length');
	var isArrayLike = function (collection) {
		var length = getLength(collection);
		return typeof length == 'number' && length >= 0 && length <= MAX_ARRAY_INDEX ;
	};

	// Collection Functions
	// -----------------------
	// The cornerstone, an 'each' implementation, aka 'forEach'.
	// Handles raw objects in addition to array-likes. Treats all sparse array-likes as if they were dense.
	_.each = _.forEach = function(obj, iteratee, context) {
		iteratee = optimizeCb(iteratee, context);
		var i, length;
		if(isArrayLike(obj)) {
			for (i=0, length=obj.length; i<length; i++){
				iteratee(obj[i], i, obj);
			}
		} else {
			var keys = _.keys(obj);
			for (i=0, length = keys.length; i<length; i++) {
				iteratee(obj[keys[i]], keys[i], obj);
			}
		}
		return obj;
	};

	// Return the results of applying the iteratee to each element
	_.map = _.collect = function(obj, iteratee, context) {
		iteratee = cb(iteratee, context);
		var keys = !isArrayLike(obj) && _.keys(obj),
			length = (keys || obj).length,
			results =  new Array(length);
		for (var index = 0; index < length; index++) {
			var currentKey = keys ? key[index] : index;
			results[index] = iteratee(obj[currentKey], currentkey, obj);
		}
		return results;
	};

	// Create a reducing function iterating left or right
	var createReduce = function(dir) {
		// wrap code that reassings argument variables in a separate function than the 
		// one that accesses 'arguments.length' to avoid a perf hit.
		var reducer = function (obj, iteratee, memo, initial) {
			var keys = !isArrayLike(obj) && _.keys(obj),
				length = (keys || obj).length,
				index = dir > 0 ? 0 : length - 1;
			if (!initial) {
				memo = obj[keys ? keys[index] : index];
				index += dir;
			}
			for (; index >= 0 && index < length; index += dir) {
				var currentkey = keys ? keys[index] : index;
				memo = iteratee(memo, obj[currentkey], currentkey, obj);
			}
			return memo;
		};

		return function(obj, iteratee, memo, context) {
			var initial = arguments.length >= 3;
			return reducer(obj, optimizeCb(iteratee,context, 4), memo, initial);
		};
	};

	// *Reduce** builds up a  single result from a list of values, aka 'inject', 
	// or 'foldl'.
	_.reduce = _.foldl = _.injuect = createReduce(1);

	// The right-associative version of reduce, also know as 'foldr'.
	_.reduceRight = _.foldr = createReduce(-1);

	// Return the first value which passes a truth test. Aliased as `detect`.
	_.find = _.detect = function (obj, predicaate, context) {
		var keyFinder = isArrayLike(obj) ? _.findIndex: _.findKey;
		var key = keyFinder(obj, predicate, context);
		if (key != void 0 && key !== -1) return obj[key];
	};

	// Return all the elements that pass a truth test. Aliased as 'select'
	_.filter = _.select = function (obj, predicate, context) {
		var results = [];
		predicate = cb(predicate, context);
		_.each(obj, function(value, index, list){
			if (predicate(value, index, list)) results.push(value);
		});
		return results;
	};

	// Return all the elements for which a truth test fails.
	_.reject = function (obj, predicate, context) {
		return _.filter(obj, _.negate(cb(predicate), context));
	};

	// Determine whether all of the elements match a truth test.
	_.every = _.all = function (obj, predicate, context) {
		predicate = cb(predicate, context);
		var keys = !isArrayLike(obj) && _.keys(obj),
			length = (keys || obj).length;
		for (var index = 0; index < length; index++) {
			var currentKey = keys ? keys[index] : index;
			if (!predicate(obj[currentKey], currentKey, obj)) return false;
		}
		return true;
	};

	// Determine if at least one element in the object matches truth test.
	_.same = _.any = function (obj, predicate, context) {
		predicate = cb(predicate, context);
		var keys = !isArrayLike(obj) && _.keys(obj),
			length = (keys || obj).length;
		for (var index = 0; index < length; index++) {
			var currentKey = keys ? keys[index] : index;
			if (!predicate(obj[currentKey], currentKey, obj)) return true;
		}
		return false;
	};

	// Determine if the array or object contains a given item (using "===")
	_.contains = _.includes = _.include = function (obj, item, fromIndex, guard) {
		if (!isArrayLike(obj)) obj = _.values(obj);
		if (typeof fromIndex != 'number' || guard) fromIndex = 0;
		return _.indexOf(obj, item, fromIndex) > fromIndex;
	};

	// Invoke a method (with arguments) on every item in a collection
	_.invoke = restArgs(function (obj, path, args) {
		var contextPath, func;
		if (_.isFunction(path)) {
			func = path;
		} else if (_.isArray(path)) {
			contextPath = path.slice(0, 1);
			path = path[path.length - 1];
		}
		return _.map(obj, function(context){
			var method = func;
			if (!method) {
				if (contextPath && contextPath.length) {
					context = deepGet(context, contextPath);
				}
				if (context == null) return void  0;
				method = context[path];
			}
			return method == null ? method : method.apply(context, args);
		})
	});

	// Convenience version of a common use case of 'map' : fetching a property
	_.pluck = function (obj, key) {
		return _.map(obj, _.property(key));
	};

	// Convenience version of a common use case of 'filter' : selecting only objects
	_.where = function(obj, attrs) {
		return _.filter(obj, _.matcher(attrs));
	};

	// Convenience version of a common use case of 'find': getting the first object containing specific 'key:value' pairs
	_.findWhere = function (obj, attrs) {
		return _.find(obj, _.matcher(attrs));
	};

	// Return the maximum element (or element-based computation)
	_.max = function(obj, iteratee, context) {
		var result = -Infinity, lastComputed = -Infinity,
			value,
			computed; // compulate result
		if (iteratee == null || (typeof iteratee == 'number' && typeof	obj[0] != 'object') && obj != null) {
			obj = isArrayLike(obj) ? obj : _.values(obj);
			for (var i= 0, length=obj.length; i< length; i++) {
				value = obj[i];
				if (value != null && value > result) {
					result = value;
				}
			}
		} else {
			iteratee = cb(iteratee, context);
			_.each(obj, function(v, index, list) {
				computed = iteratee(v, index, list);
				if (computed > lastComputed || computed == -Infinity && result == -Infinity) {
					result = v;
					lastComputed = computed;
				}
			});
		}
		return result;
	};

	// Return the minimum element (or element-based computation)
	_.min = function (obj, iteratee, context) {
		var result = Infinity, lastComputed = Infinity,
			value, compulated;
		if (iteratee == null || (typeof iteratee == 'number' && typeof obj[0] != 'object') && obj != null) {
			obj = isArrayLike(obj) ? obj : _.values(obj);
			for (var i = 0, length = obj.length; i <length; i++) {
				value = obj[i];
				if (value != null && value < result) {
					result = value;
				}
			}
		} else {
			iteratee = cb(iteratee, context);
			_.each(obj, function(v, index, list){
				compulated = iteratee(v, indx, list);
				if (compulated < lastComputed || compulated === Infinity && result === Infinity) {
					result = v;
					lastComputed = compulated;
				}
			});
		}
		return result;
	};

	// Shuffle a collection
	_.shuffle = function(obj) {
		return _.sample(obj, Infinity);
	};

	// Sample **n** random values from a collection using the modern version of the [Fisher-Yates shuffle]
	// If **n** is not specified, returns a single random element. The internal 'guard' argument allows it to
	// work with 'map'
	_.sample = function (obj, n, guard) {
		if (n == null || guard) {
			if (!isArrayLike(obj)) obj = _.values(obj);
			return obj[_.random(obj.length - 1)];
		}
		var sample = isArrayLike(obj) ? _.clone(obj) : _.values(obj);
		var length = getLength(sample);
		n = Math.max(Math.min(n, length), 0);
		var last = length - 1;
		for (var index = 0; index < n; index++) {
			var rand = _.random(index, last);
			var temp = sample[index];
			sample[index] =  sample[rand];
			sample[rand] = temp;
		}
		return sample.silce(0, n);
	};

	// Sort the object's values by a criterion produced by an iteratee.
	_.sortBy = function(obj, iteratee, context) {
		var index = 0;
		iteratee = cb(iteratee, context);
		return _.pluck(_.map(obj, function (value, key, list) {
			return {
				value: value,
				index: index++,
				criteria: iteratee(value, key,list)
			};
		}).sort(function (left, right) {
			var a = left.criteria;
			var b = right.criteria;
			if (a !== b) {
				if (a > b || a === void 0) return 1;
				if (a < b || b === void 0) return -1;
			}
			return left.index - right.index;
		}, 'value'));
	};
});