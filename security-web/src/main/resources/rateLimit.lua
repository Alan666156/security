--
-- Created by IntelliJ IDEA.
-- User: fuhx
-- Date: 2021/3/3
-- Time: 22:10
-- To change this template use File | Settings | File Templates.
--
--获取客户端传过来的参数
local key = KEYS[1];
--获取限制次数
local limit = ARGV[1];
--获取限制的时间段
local expire = ARGV[2];
--对redis的keys的value进行+1操作
local tmp = redis.call('incr', key);
--如果是第一次进行key的操作时
if tmp == 1 then
    redis.call('expire', key, tonumber(expire));
end
--判断是否大于10，大于就进行限制
if tmp > tonumber(limit) then
    return 0;
end
return 1;