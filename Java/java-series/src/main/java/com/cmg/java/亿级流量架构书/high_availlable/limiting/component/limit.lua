local key = KEYS[1]  --限流KEY 一秒一个
local limit = tonumber(ARGV[1]) --限流大小
--请求数加1
--local current = tonumber(redis.call("INCRBY", key, "1"))
--if current > limit then
--    return 0
--    --只有第一次访问需要设置2秒过期时间
--elseif curren == 1 then
--    redis.call("expire", key, "2")
--end
--return 1
local current = tonumber(redis.call("get", key) or "1")
if current + 1 > limit then
    return 0
else
    redis.call("INCRBY", key, "1")
    redis.call("expire", key, "2")
end

