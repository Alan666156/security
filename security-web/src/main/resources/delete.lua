--
-- 删除
-- User: fuhongxing
-- Date: 2021/3/16
-- Time: 9:56 上午
-- To change this template use File | Settings | File Templates.
--

if redis.call('get', KEYS[1]) == ARGV[1]
then
    return redis.call('del', KEYS[1])
else
    return 0
end

