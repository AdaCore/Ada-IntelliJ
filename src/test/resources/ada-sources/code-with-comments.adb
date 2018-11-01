
-- This is an Ada single-line comment
-- Each of these two lines should result in a separate comment token
function test return Integer is
begin
	-------------------
	-- Function body --
	-------------------
	return 42;
end test;

-- This comment should represent the last token in the file