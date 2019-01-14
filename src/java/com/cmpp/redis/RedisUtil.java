package com.cmpp.redis;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
	private JedisPool pool = null;
	private static String ip="";
	private static String password="";
	private static int prot=0;
	private static int timout=10000;
	
	 /**
	   * <p>传入ip和端口号构建redis 连接池</p>
	   * @param ip ip
	   * @param prot 端口
	 * @throws IOException 
	   */
	public RedisUtil() throws IOException {
	    if (pool == null) { 
	    	//--------------本地main----------------
		  /*ip="192.168.10.93";
		  prot=7000;
		  password="ztredis2017";
		  timout=10000;
		  
	      JedisPoolConfig config = new JedisPoolConfig();
	      //lifo：borrowObject返回对象时，是采用DEFAULT_LIFO（last in first out，即类似cache的最频繁使用队列），如果为False，则表示FIFO队列；
	      config.lifo=true;
	      //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	      config.setMaxActive(500);
	      //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
	      config.setMaxWait(100000);
	      //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
	      config.setMinEvictableIdleTimeMillis(1800000);
	      //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例
	      config.setMaxIdle(5);
	      //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	      config.setTestOnBorrow(true);
	      //如果为true，表示有一个idle object evitor线程对idle object进行扫描，如果validate失败，此object会被从pool中drop掉；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
	      config.setTestWhileIdle(true);
	      //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
	      config.setTimeBetweenEvictionRunsMillis(30000);
	      //表示idle object evitor每次扫描的最多的对象数；每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
          config.setNumTestsPerEvictionRun(3);
	      //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
	      config.setSoftMinEvictableIdleTimeMillis(1800000);
	   */
	    	
	    	//----------服务器-----------
	    	 ip=System.getProperty("redis.ip");
			  prot=Integer.parseInt(System.getProperty("redis.port"));
			  password=System.getProperty("redis.password");
			  timout=Integer.parseInt(System.getProperty("redis.jedispooltimeout"));
			  
		      JedisPoolConfig config = new JedisPoolConfig();
		      //lifo：borrowObject返回对象时，是采用DEFAULT_LIFO（last in first out，即类似cache的最频繁使用队列），如果为False，则表示FIFO队列；
		      config.lifo=true;
		      //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
		      config.setMaxActive(Integer.parseInt(System.getProperty("redis.maxtotal")));
		      //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
		      config.setMaxWait(Long.parseLong(System.getProperty("redis.maxwaitmillis")));
		      //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
		      //config.setMinEvictableIdleTimeMillis(Long.parseLong(System.getProperty("redis.minevictableidletimemillis")));
		      //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例
		      config.setMaxIdle(Integer.parseInt(System.getProperty("redis.maxidle")));
		      //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
		      config.setTestOnBorrow(true);
		      //如果为true，表示有一个idle object evitor线程对idle object进行扫描，如果validate失败，此object会被从pool中drop掉；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
		      config.setTestWhileIdle(true);
		      //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
		      //config.setTimeBetweenEvictionRunsMillis(Long.parseLong(System.getProperty("redis.timebetweenevictionrunsmillis")));
		      //表示idle object evitor每次扫描的最多的对象数；每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
	          //config.setNumTestsPerEvictionRun(Integer.parseInt(System.getProperty("redis.numtestsperevictionrun")));
		      //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
		      //config.setSoftMinEvictableIdleTimeMillis(Long.parseLong(System.getProperty("redis.softminevictableidletimemillis")));
	      
	    	
	    	
	    	//----------服务器-----------
	    	/*String configheadjson = System.getProperty("confighead");
	    	Confighead confighead = new Gson().fromJson(configheadjson, Confighead.class);
	    	
	    	 ip=confighead.getRedis_ip();
			  prot=Integer.parseInt(confighead.getRedis_port());
			  password=confighead.getRedis_password();
			  timout = Integer.parseInt(confighead.getRedis_jedispooltimeout());
			  
		      JedisPoolConfig config = new JedisPoolConfig();
		      //lifo：borrowObject返回对象时，是采用DEFAULT_LIFO（last in first out，即类似cache的最频繁使用队列），如果为False，则表示FIFO队列；
		      config.lifo=true;
		      //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
		      config.setMaxActive(Integer.parseInt(confighead.getRedis_maxtotal()));
		      //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
		      config.setMaxWait(Long.parseLong(confighead.getRedis_maxwaitmillis()));
		      //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
		      //config.setMinEvictableIdleTimeMillis(Long.parseLong(System.getProperty("redis.minevictableidletimemillis")));
		      //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例
		      config.setMaxIdle(Integer.parseInt(confighead.getRedis_maxidle()));
		      //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
		      config.setTestOnBorrow(true);
		      //如果为true，表示有一个idle object evitor线程对idle object进行扫描，如果validate失败，此object会被从pool中drop掉；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
		      config.setTestWhileIdle(true);
		      //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
		      //config.setTimeBetweenEvictionRunsMillis(Long.parseLong(System.getProperty("redis.timebetweenevictionrunsmillis")));
		      //表示idle object evitor每次扫描的最多的对象数；每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
	          //config.setNumTestsPerEvictionRun(Integer.parseInt(System.getProperty("redis.numtestsperevictionrun")));
		      //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
		      //config.setSoftMinEvictableIdleTimeMillis(Long.parseLong(System.getProperty("redis.softminevictableidletimemillis")));
	      */
	    	
	      pool = new JedisPool(config, ip, prot, timout,password);
	    }
	  }
	private static RedisUtil instance = null;
	
	/**
	 * 16个库的用途
	 * 0：存签名自动匹配扩展   key：签名  ，value：扩展号
	 * 1：接口提交用户余额不足用户名   
	 * 2：拦截疑似轰炸信息用户名号码
	 * 3：管理商自动充值IP鉴权绑定
	 * @return
	 * 
	 */
	public static RedisUtil getInstance(){
		if (instance == null) {
			try {
				instance = new RedisUtil();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}	
	  /**
	   * <p>通过key获取储存在redis中的value</p>
	   * <p>并释放连接</p>
	   * @param key
	   * @return 成功返回value 失败返回null
	   */
	public String get(String key){
	    Jedis jedis = null;
	    String value = null;
	    try {
	      jedis = pool.getResource();
	      value = jedis.get(key);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return value;
	  }
	public String get(String key,int indexv){
		Jedis jedis = null;
		String value = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			value = jedis.get(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return value;
	}
	  
	  /**
	   * <p>向redis存入key和value,并释放连接资源</p>
	   * <p>如果key已经存在 则覆盖</p>
	   * @param key
	   * @param value
	   * @return 成功 返回OK 失败返回 0
	   */
	public String set(String key,String value){
	    Jedis jedis = null;
	    try {
	      jedis = pool.getResource();
	      return jedis.set(key, value);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	      return "0";
	    } finally {
	      returnResource(pool, jedis);
	    }
	  }
	public String set(String key,String value,int indexv){
	    Jedis jedis = null;
	    try {
	      jedis = pool.getResource();
	      jedis.select(indexv);
	      return jedis.set(key, value);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	      return "0";
	    } finally {
	      returnResource(pool, jedis);
	    }
	  }
	  /**
	   * <p>删除指定的key,也可以传入一个包含key的数组</p>
	   * @param keys 一个key  也可以使 string 数组
	   * @return 返回删除成功的个数 
	   */
	public Long del(String...keys){
	    Jedis jedis = null;
	    try {
	      jedis = pool.getResource();
	      return jedis.del(keys);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	      return 0L;
	    } finally {
	      returnResource(pool, jedis);
	    }
	  }
	public Long del(int indexv,String...keys){
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			return jedis.del(keys);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
			return 0L;
		} finally {
			returnResource(pool, jedis);
		}
	}
	  
	  /**
	   * <p>通过key向指定的value值追加值</p>
	   * @param key 
	   * @param str 
	   * @return 成功返回 添加后value的长度 失败 返回 添加的 value 的长度  异常返回0L
	   */
	public Long append(String key ,String str){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.append(key, str);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	      return 0L;
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long append(String key ,String str,int indexv){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      jedis.select(indexv);
	      res = jedis.append(key, str);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	      return 0L;
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	  
	  /**
	   * <p>判断key是否存在</p>
	   * @param key
	   * @return true OR false
	   */
	public Boolean exists(String key){
	    Jedis jedis = null;
	    try {
	      jedis = pool.getResource();
	      return jedis.exists(key);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	      return false;
	    } finally {
	      returnResource(pool, jedis);
	    }
	  }
	public Boolean exists(String key,int indexv){
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			return jedis.exists(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
			return false;
		} finally {
			returnResource(pool, jedis);
		}
	}
	  
	  /**
	   * <p>设置key value,如果key已经存在则返回0,nx==> not exist</p>
	   * @param key
	   * @param value
	   * @return 成功返回1 如果存在 和 发生异常 返回 0
	   */
	public Long setnx(String key ,String value){
	    Jedis jedis = null;
	    try {
	      jedis = pool.getResource();
	      return jedis.setnx(key, value);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	      return 0L;
	    } finally {
	      returnResource(pool, jedis);
	    }
	  }
	public Long setnx(String key ,String value,int indexv){
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			return jedis.setnx(key, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
			return 0L;
		} finally {
			returnResource(pool, jedis);
		}
	}
	  
	  /**
	   * <p>设置key value并制定这个键值的有效期</p>
	   * @param key
	   * @param value
	   * @param seconds 单位:秒
	   * @return 成功返回OK 失败和异常返回null
	   */
	public String setex(String key,String value,int seconds){
	    Jedis jedis = null;
	    String res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.setex(key, seconds, value);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public String setex(String key,String value,int seconds,int indexv){
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.setex(key, seconds, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key 和offset 从指定的位置开始将原先value替换</p>
	   * <p>下标从0开始,offset表示从offset下标开始替换</p>
	   * <p>如果替换的字符串长度过小则会这样</p>
	   * <p>example:</p>
	   * <p>value : bigsea@zto.cn</p>
	   * <p>str : abc </p>
	   * <P>从下标7开始替换  则结果为</p>
	   * <p>RES : bigsea.abc.cn</p>
	   * @param key
	   * @param str
	   * @param offset 下标位置
	   * @return 返回替换后  value 的长度
	   */
	public Long setrange(String key,String str,int offset){
	    Jedis jedis = null;
	    try {
	      jedis = pool.getResource();
	      return jedis.setrange(key, offset, str);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	      return 0L;
	    } finally {
	      returnResource(pool, jedis);
	    }
	  }
	public Long setrange(String key,String str,int offset,int indexv){
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			return jedis.setrange(key, offset, str);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
			return 0L;
		} finally {
			returnResource(pool, jedis);
		}
	}
	  
	  /**
	   * <p>通过批量的key获取批量的value</p>
	   * @param keys string数组 也可以是一个key
	   * @return 成功返回value的集合, 失败返回null的集合 ,异常返回空
	   */
	public List<String> mget(String...keys){
	    Jedis jedis = null;
	    List<String> values = null;
	    try {
	      jedis = pool.getResource();
	      values = jedis.mget(keys);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return values;
	  }
	public List<String> mget(int indexv,String...keys){
		Jedis jedis = null;
		List<String> values = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			values = jedis.mget(keys);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return values;
	}
	  
	  /**
	   * <p>批量的设置key:value,可以一个</p>
	   * <p>example:</p>
	   * <p>  obj.mset(new String[]{"key2","value1","key2","value2"})</p>
	   * @param keysvalues
	   * @return 成功返回OK 失败 异常 返回 null
	   * 
	   */
	public String mset(String...keysvalues){
	    Jedis jedis = null;
	    String res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.mset(keysvalues);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public String mset(int indexv,String...keysvalues){
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.mset(keysvalues);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>批量的设置key:value,可以一个,如果key已经存在则会失败,操作会回滚</p>
	   * <p>example:</p>
	   * <p>  obj.msetnx(new String[]{"key2","value1","key2","value2"})</p>
	   * @param keysvalues 
	   * @return 成功返回1 失败返回0 
	   */
	public Long msetnx(String...keysvalues){
	    Jedis jedis = null;
	    Long res = 0L;
	    try {
	      jedis = pool.getResource();
	      res =jedis.msetnx(keysvalues);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long msetnx(int indexv,String...keysvalues){
		Jedis jedis = null;
		Long res = 0L;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res =jedis.msetnx(keysvalues);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>设置key的值,并返回一个旧值</p>
	   * @param key
	   * @param value
	   * @return 旧值 如果key不存在 则返回null
	   */
	public String getset(String key,String value){
	    Jedis jedis = null;
	    String res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.getSet(key, value);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public String getset(String key,String value,int indexv){
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.getSet(key, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过下标 和key 获取指定下标位置的 value</p>
	   * @param key
	   * @param startOffset 开始位置 从0 开始 负数表示从右边开始截取
	   * @param endOffset 
	   * @return 如果没有返回null 
	   */
	public String getrange(String key, int startOffset ,int endOffset){
	    Jedis jedis = null;
	    String res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.getrange(key, startOffset, endOffset);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public String getrange(int indexv,String key, int startOffset ,int endOffset){
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.getrange(key, startOffset, endOffset);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key 对value进行加值+1操作,当value不是int类型时会返回错误,当key不存在是则value为1</p>
	   * @param key
	   * @return 加值后的结果
	   */
	public Long incr(String key){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.incr(key);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long incr(int indexv,String key){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.incr(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	/**
	 * 设置key的有效时间，超过时间自动清除，成功返回1，key为null返回0
	 * @param key
	 * @return
	 */
	public Long expire(String key,int seconds){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.expire(key, seconds);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long expire(int indexv,String key,int seconds){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      jedis.select(indexv);
	      res = jedis.expire(key, seconds);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	  /**
	   * <p>通过key给指定的value加值,如果key不存在,则这是value为该值</p>
	   * @param key
	   * @param integer
	   * @return
	   */
	public Long incrBy(String key,Long integer){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.incrBy(key, integer);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long incrBy(int indexv,String key,Long integer){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.incrBy(key, integer);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>对key的值做减减操作,如果key不存在,则设置key为-1</p>
	   * @param key
	   * @return
	   */
	public Long decr(String key) {
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.decr(key);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long decr(int indexv,String key) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.decr(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>减去指定的值</p>
	   * @param key
	   * @param integer
	   * @return
	   */
	public Long decrBy(String key,Long integer){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.decrBy(key, integer);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long decrBy(int indexv,String key,Long integer){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.decrBy(key, integer);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key获取value值的长度</p>
	   * @param key
	   * @return 失败返回null 
	   */
	public Long serlen(String key){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.strlen(key);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long serlen(int indexv,String key){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.strlen(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key给field设置指定的值,如果key不存在,则先创建</p>
	   * @param key
	   * @param field 字段
	   * @param value
	   * @return 如果存在返回0 异常返回null
	   */
	public Long hset(String key,String field,String value) {
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.hset(key, field, value);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long hset(int indexv,String key,String field,String value) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.hset(key, field, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key给field设置指定的值,如果key不存在则先创建,如果field已经存在,返回0</p>
	   * @param key
	   * @param field
	   * @param value
	   * @return
	   */
	public Long hsetnx(String key,String field,String value){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.hsetnx(key, field, value);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long hsetnx(int indexv,String key,String field,String value){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.hsetnx(key, field, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key同时设置 hash的多个field</p>
	   * @param key
	   * @param hash
	   * @return 返回OK 异常返回null
	   */
	public String hmset(String key,Map<String, String> hash){
	    Jedis jedis = null;
	    String res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.hmset(key, hash);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public String hmset(int indexv,String key,Map<String, String> hash){
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.hmset(key, hash);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key 和 field 获取指定的 value</p>
	   * @param key
	   * @param field
	   * @return 没有返回null
	   */
	public String hget(String key, String field){
	    Jedis jedis = null;
	    String res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.hget(key, field);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public String hget(int indexv,String key, String field){
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.hget(key, field);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key 和 fields 获取指定的value 如果没有对应的value则返回null</p>
	   * @param key
	   * @param fields 可以使 一个String 也可以是 String数组
	   * @return 
	   */
	public List<String> hmget(String key,String...fields){
	    Jedis jedis = null;
	    List<String> res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.hmget(key, fields);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public List<String> hmget(int indexv,String key,String...fields){
		Jedis jedis = null;
		List<String> res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.hmget(key, fields);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key给指定的field的value加上给定的值</p>
	   * @param key
	   * @param field
	   * @param value 
	   * @return
	   */
	public Long hincrby(String key ,String field ,Long value){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.hincrBy(key, field, value);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long hincrby(int indexv,String key ,String field ,Long value){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.hincrBy(key, field, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key和field判断是否有指定的value存在</p>
	   * @param key
	   * @param field
	   * @return
	   */
	public Boolean hexists(String key , String field){
	    Jedis jedis = null;
	    Boolean res = false;
	    try {
	      jedis = pool.getResource();
	      res = jedis.hexists(key, field);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Boolean hexists(int indexv,String key , String field){
		Jedis jedis = null;
		Boolean res = false;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.hexists(key, field);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key返回field的数量</p>
	   * @param key
	   * @return
	   */
	public Long hlen(String key){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.hlen(key);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	    
	  }
	public Long hlen(int indexv,String key){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.hlen(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
		
	}
	  
	  /**
	   * <p>通过key 删除指定的 field </p>
	   * @param key
	   * @param fields 可以是 一个 field 也可以是 一个数组
	   * @return
	   */
	public Long hdel(String key ,String...fields){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.hdel(key, fields);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long hdel(int indexv,String key ,String...fields){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.hdel(key, fields);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key返回所有的field</p>
	   * @param key
	   * @return
	   */
	public Set<String> hkeys(String key){
	    Jedis jedis = null;
	    Set<String> res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.hkeys(key);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Set<String> hkeys(int indexv,String key){
		Jedis jedis = null;
		Set<String> res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.hkeys(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key返回所有和key有关的value</p>
	   * @param key
	   * @return
	   */
	public List<String> hvals(String key){
	    Jedis jedis = null;
	    List<String> res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.hvals(key);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public List<String> hvals(int indexv,String key){
		Jedis jedis = null;
		List<String> res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.hvals(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key获取所有的field和value</p>
	   * @param key
	   * @return
	   */
	public Map<String, String> hgetall(String key){
	    Jedis jedis = null;
	    Map<String, String> res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.hgetAll(key);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Map<String, String> hgetall(int indexv,String key){
		Jedis jedis = null;
		Map<String, String> res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.hgetAll(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key向list头部添加字符串</p>
	   * @param key
	   * @param strs 可以使一个string 也可以使string数组
	   * @return 返回list的value个数
	   */
	public Long lpush(String key ,String...strs){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.lpush(key, strs);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long lpush(int indexv,String key ,String...strs){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.lpush(key, strs);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key向list尾部添加字符串</p>
	   * @param key
	   * @param strs 可以使一个string 也可以使string数组
	   * @return 返回list的value个数
	   */
	public Long rpush(String key ,String...strs){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.rpush(key, strs);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long rpush(int indexv,String key ,String...strs){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.rpush(key, strs);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key在list指定的位置之前或者之后 添加字符串元素</p>
	   * @param key 
	   * @param where LIST_POSITION枚举类型
	   * @param pivot list里面的value
	   * @param value 添加的value
	   * @return
	   */
	public Long linsert(String key, LIST_POSITION where,
	        String pivot, String value){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.linsert(key, where, pivot, value);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long linsert(int indexv,String key, LIST_POSITION where,
			String pivot, String value){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.linsert(key, where, pivot, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key设置list指定下标位置的value</p>
	   * <p>如果下标超过list里面value的个数则报错</p>
	   * @param key 
	   * @param index 从0开始
	   * @param value
	   * @return 成功返回OK
	   */
	public String lset(String key ,Long index, String value){
	    Jedis jedis = null;
	    String res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.lset(key, index, value);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public String lset(int indexv,String key ,Long index, String value){
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.lset(key, index, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key从对应的list中删除指定的count个 和 value相同的元素</p>
	   * @param key 
	   * @param count 当count为0时删除全部
	   * @param value 
	   * @return 返回被删除的个数
	   */
	public Long lrem(String key,long count,String value){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.lrem(key, count, value);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long lrem(int indexv,String key,long count,String value){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.lrem(key, count, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key保留list中从strat下标开始到end下标结束的value值</p>
	   * @param key
	   * @param start
	   * @param end
	   * @return 成功返回OK
	   */
	public String ltrim(String key ,long start ,long end){
	    Jedis jedis = null;
	    String res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.ltrim(key, start, end);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public String ltrim(int indexv,String key ,long start ,long end){
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.ltrim(key, start, end);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key从list的头部删除一个value,并返回该value</p>
	   * @param key
	   * @return 
	   */
	public String lpop(String key){
	    Jedis jedis = null;
	    String res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.lpop(key);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public String lpop(int indexv,String key){
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.lpop(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key从list尾部删除一个value,并返回该元素</p>
	   * @param key
	   * @return
	   */
	public String rpop(String key){
	    Jedis jedis = null;
	    String res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.rpop(key);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public String rpop(int indexv,String key){
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.rpop(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key从一个list的尾部删除一个value并添加到另一个list的头部,并返回该value</p>
	   * <p>如果第一个list为空或者不存在则返回null</p>
	   * @param srckey
	   * @param dstkey
	   * @return
	   */
	public String rpoplpush(String srckey, String dstkey){
	    Jedis jedis = null;
	    String res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.rpoplpush(srckey, dstkey);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public String rpoplpush(int indexv,String srckey, String dstkey){
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.rpoplpush(srckey, dstkey);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key获取list中指定下标位置的value</p>
	   * @param key
	   * @param index
	   * @return 如果没有返回null
	   */
	public String lindex(String key,long index){
	    Jedis jedis = null;
	    String res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.lindex(key, index);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public String lindex(int indexv,String key,long index){
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.lindex(key, index);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key返回list的长度</p>
	   * @param key
	   * @return
	   */
	public Long llen(String key){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.llen(key);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long llen(int indexv,String key){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.llen(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key获取list指定下标位置的value</p>
	   * <p>如果start 为 0 end 为 -1 则返回全部的list中的value</p>
	   * @param key 
	   * @param start
	   * @param end
	   * @return
	   */
	public List<String> lrange(String key,long start,long end){
	    Jedis jedis = null;
	    List<String> res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.lrange(key, start, end);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public List<String> lrange(int indexv,String key,long start,long end){
		Jedis jedis = null;
		List<String> res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.lrange(key, start, end);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key向指定的set中添加value</p>
	   * @param key
	   * @param members 可以是一个String 也可以是一个String数组
	   * @return 添加成功的个数
	   */
	public Long sadd(String key,String...members){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.sadd(key, members);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long sadd(int indexv,String key,String...members){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.sadd(key, members);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key删除set中对应的value值</p>
	   * @param key
	   * @param members 可以是一个String 也可以是一个String数组
	   * @return 删除的个数
	   */
	public Long srem(String key,String...members){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.srem(key, members);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long srem(int indexv,String key,String...members){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.srem(key, members);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key随机删除一个set中的value并返回该值</p>
	   * @param key
	   * @return
	   */
	public String spop(String key){
	    Jedis jedis = null;
	    String res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.spop(key);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public String spop(int indexv,String key){
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.spop(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key获取set中的差集</p>
	   * <p>以第一个set为标准</p>
	   * @param keys 可以使一个string 则返回set中所有的value 也可以是string数组
	   * @return 
	   */
	public Set<String> sdiff(String...keys){
	    Jedis jedis = null;
	    Set<String> res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.sdiff(keys);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Set<String> sdiff(int indexv,String...keys){
		Jedis jedis = null;
		Set<String> res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.sdiff(keys);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key获取set中的差集并存入到另一个key中</p>
	   * <p>以第一个set为标准</p>
	   * @param dstkey 差集存入的key
	   * @param keys 可以使一个string 则返回set中所有的value 也可以是string数组
	   * @return 
	   */
	public Long sdiffstore(String dstkey,String... keys){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.sdiffstore(dstkey, keys);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long sdiffstore(int indexv,String dstkey,String... keys){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.sdiffstore(dstkey, keys);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key获取指定set中的交集</p>
	   * @param keys 可以使一个string 也可以是一个string数组
	   * @return
	   */
	public Set<String> sinter(String...keys){
	    Jedis jedis = null;
	    Set<String> res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.sinter(keys);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Set<String> sinter(int indexv,String...keys){
		Jedis jedis = null;
		Set<String> res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.sinter(keys);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key获取指定set中的交集 并将结果存入新的set中</p>
	   * @param dstkey
	   * @param keys 可以使一个string 也可以是一个string数组
	   * @return
	   */
	public Long sinterstore(String dstkey,String...keys){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.sinterstore(dstkey, keys);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long sinterstore(int indexv,String dstkey,String...keys){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.sinterstore(dstkey, keys);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key返回所有set的并集</p>
	   * @param keys 可以使一个string 也可以是一个string数组
	   * @return
	   */
	public Set<String> sunion(String... keys){
	    Jedis jedis = null;
	    Set<String> res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.sunion(keys);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Set<String> sunion(int indexv,String... keys){
		Jedis jedis = null;
		Set<String> res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.sunion(keys);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key返回所有set的并集,并存入到新的set中</p>
	   * @param dstkey 
	   * @param keys 可以使一个string 也可以是一个string数组
	   * @return
	   */
	public Long sunionstore(String dstkey,String...keys){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.sunionstore(dstkey, keys);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long sunionstore(int indexv,String dstkey,String...keys){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.sunionstore(dstkey, keys);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key将set中的value移除并添加到第二个set中</p>
	   * @param srckey 需要移除的
	   * @param dstkey 添加的
	   * @param member set中的value
	   * @return
	   */
	public Long smove(String srckey, String dstkey, String member){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.smove(srckey, dstkey, member);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long smove(int indexv,String srckey, String dstkey, String member){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.smove(srckey, dstkey, member);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key获取set中value的个数</p>
	   * @param key
	   * @return
	   */
	public Long scard(String key){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.scard(key);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long scard(int indexv,String key){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.scard(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key判断value是否是set中的元素</p>
	   * @param key
	   * @param member
	   * @return
	   */
	public Boolean sismember(String key,String member){
	    Jedis jedis = null;
	    Boolean res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.sismember(key, member);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Boolean sismember(int indexv,String key,String member){
		Jedis jedis = null;
		Boolean res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.sismember(key, member);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key获取set中随机的value,不删除元素</p>
	   * @param key
	   * @return
	   */
	public String srandmember(String key){
	    Jedis jedis = null;
	    String res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.srandmember(key);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public String srandmember(int indexv,String key){
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.srandmember(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key获取set中所有的value</p>
	   * @param key
	   * @return
	   */
	public Set<String> smembers(String key){
	    Jedis jedis = null;
	    Set<String> res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.smembers(key);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Set<String> smembers(int indexv,String key){
		Jedis jedis = null;
		Set<String> res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.smembers(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key向zset中添加value,score,其中score就是用来排序的</p>
	   * <p>如果该value已经存在则根据score更新元素</p>
	   * @param key
	   * @param scoreMembers 
	   * @return
	   */
	public Long zadd(String key,Map<Double,String> scoreMembers){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.zadd(key, scoreMembers);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long zadd(int indexv,String key,Map<Double,String> scoreMembers){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.zadd(key, scoreMembers);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key向zset中添加value,score,其中score就是用来排序的</p>
	   * <p>如果该value已经存在则根据score更新元素</p>
	   * @param key
	   * @param score
	   * @param member
	   * @return
	   */
	public Long zadd(String key,double score,String member){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.zadd(key, score, member);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long zadd(int indexv,String key,double score,String member){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.zadd(key, score, member);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key删除在zset中指定的value</p>
	   * @param key
	   * @param members 可以使一个string 也可以是一个string数组
	   * @return
	   */
	public Long zrem(String key,String...members){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.zrem(key, members);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long zrem(int indexv,String key,String...members){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.zrem(key, members);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key增加该zset中value的score的值</p>
	   * @param key
	   * @param score 
	   * @param member 
	   * @return
	   */
	public Double zincrby(String key ,double score ,String member){
	    Jedis jedis = null;
	    Double res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.zincrby(key, score, member);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Double zincrby(int indexv,String key ,double score ,String member){
		Jedis jedis = null;
		Double res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.zincrby(key, score, member);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key返回zset中value的排名</p>
	   * <p>下标从小到大排序</p>
	   * @param key
	   * @param member
	   * @return
	   */
	public Long zrank(String key,String member){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.zrank(key, member);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long zrank(int indexv,String key,String member){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.zrank(key, member);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key返回zset中value的排名</p>
	   * <p>下标从大到小排序</p>
	   * @param key
	   * @param member
	   * @return
	   */
	public Long zrevrank(String key,String member){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.zrevrank(key, member);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long zrevrank(int indexv,String key,String member){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.zrevrank(key, member);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key将获取score从start到end中zset的value</p>
	   * <p>socre从大到小排序</p>
	   * <p>当start为0 end为-1时返回全部</p>
	   * @param key
	   * @param start
	   * @param end
	   * @return
	   */
	public Set<String> zrevrange(String key ,long start ,long end){
	    Jedis jedis = null;
	    Set<String> res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.zrevrange(key, start, end);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Set<String> zrevrange(int indexv,String key ,long start ,long end){
		Jedis jedis = null;
		Set<String> res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.zrevrange(key, start, end);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key返回指定score内zset中的value</p>
	   * @param key 
	   * @param max 
	   * @param min 
	   * @return
	   */
	public Set<String> zrangebyscore(String key,String max,String min){
	    Jedis jedis = null;
	    Set<String> res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.zrevrangeByScore(key, max, min);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Set<String> zrangebyscore(int indexv,String key,String max,String min){
		Jedis jedis = null;
		Set<String> res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.zrevrangeByScore(key, max, min);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key返回指定score内zset中的value</p>
	   * @param key 
	   * @param max  
	   * @param min 
	   * @return
	   */
	public Set<String> zrangeByScore(String key ,double max,double min){
	    Jedis jedis = null;
	    Set<String> res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.zrevrangeByScore(key,max,min);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Set<String> zrangeByScore(int indexv,String key ,double max,double min){
		Jedis jedis = null;
		Set<String> res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.zrevrangeByScore(key,max,min);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>返回指定区间内zset中value的数量</p>
	   * @param key
	   * @param min
	   * @param max
	   * @return
	   */
	public Long zcount(String key,String min,String max){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.zcount(key, min, max);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long zcount(int indexv,String key,String min,String max){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.zcount(key, min, max);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key返回zset中的value个数</p>
	   * @param key
	   * @return
	   */
	public Long zcard(String key){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.zcard(key);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long zcard(int indexv,String key){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.zcard(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key获取zset中value的score值</p>
	   * @param key
	   * @param member
	   * @return
	   */
	public Double zscore(String key,String member){
	    Jedis jedis = null;
	    Double res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.zscore(key, member);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Double zscore(int indexv,String key,String member){
		Jedis jedis = null;
		Double res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.zscore(key, member);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key删除给定区间内的元素</p>
	   * @param key 
	   * @param start 
	   * @param end
	   * @return
	   */
	public Long zremrangeByRank(String key ,long start, long end){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.zremrangeByRank(key, start, end);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long zremrangeByRank(int indexv,String key ,long start, long end){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.zremrangeByRank(key, start, end);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key删除指定score内的元素</p>
	   * @param key
	   * @param start
	   * @param end
	   * @return
	   */
	public Long zremrangeByScore(String key,double start,double end){
	    Jedis jedis = null;
	    Long res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.zremrangeByScore(key, start, end);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Long zremrangeByScore(int indexv,String key,double start,double end){
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.zremrangeByScore(key, start, end);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>返回满足pattern表达式的所有key</p>
	   * <p>keys(*)</p>
	   * <p>返回所有的key</p>
	   * @param pattern
	   * @return
	   */
	public Set<String> keys(String pattern){
	    Jedis jedis = null;
	    Set<String> res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.keys(pattern);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public Set<String> keys(int indexv,String pattern){
		Jedis jedis = null;
		Set<String> res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.keys(pattern);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  /**
	   * <p>通过key判断值得类型</p>
	   * @param key
	   * @return
	   */
	public String type(String key){
	    Jedis jedis = null;
	    String res = null;
	    try {
	      jedis = pool.getResource();
	      res = jedis.type(key);
	    } catch (Exception e) {
	      pool.returnBrokenResource(jedis);
	      e.printStackTrace();
	    } finally {
	      returnResource(pool, jedis);
	    }
	    return res;
	  }
	public String type(int indexv,String key){
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			jedis.select(indexv);
			res = jedis.type(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	  
	  
	  /**
	   * 返还到连接池
	   * 
	   * @param pool
	   * @param redis
	   */
	public static void returnResource(JedisPool pool, Jedis jedis) {
	    if (jedis != null) {
	      pool.returnResource(jedis);
	    }
	  }
}
