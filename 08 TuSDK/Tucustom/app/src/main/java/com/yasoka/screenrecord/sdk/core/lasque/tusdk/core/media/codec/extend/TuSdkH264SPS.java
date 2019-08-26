// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.Arrays;
//import org.lasque.tusdk.core.utils.TLog;

public class TuSdkH264SPS
{
    public final byte[] data;
    public boolean isSps;
    public int width;
    public int height;
    public int fps;
    public int dar_width;
    public int dar_height;
    public int forbidden_zero_bit;
    public int nal_ref_idc;
    public int nal_unit_type;
    public int profile_idc;
    public int constraint_set0_flag;
    public int constraint_set1_flag;
    public int constraint_set2_flag;
    public int constraint_set3_flag;
    public int reserved_zero_4bits;
    public int level_idc;
    public int seq_parameter_set_id;
    public int chroma_format_idc;
    public int residual_colour_transform_flag;
    public int bit_depth_luma_minus8;
    public int bit_depth_chroma_minus8;
    public int qpprime_y_zero_transform_bypass_flag;
    public int seq_scaling_matrix_present_flag;
    public int[] seq_scaling_list_present_flag;
    public int log2_max_frame_num_minus4;
    public int pic_order_cnt_type;
    public int log2_max_pic_order_cnt_lsb_minus4;
    public int delta_pic_order_always_zero_flag;
    public int offset_for_non_ref_pic;
    public int offset_for_top_to_bottom_field;
    public int num_ref_frames_in_pic_order_cnt_cycle;
    public int num_ref_frames;
    public int gaps_in_frame_num_value_allowed_flag;
    public int pic_width_in_mbs_minus1;
    public int pic_height_in_map_units_minus1;
    public int frame_mbs_only_flag;
    public int mb_adaptive_frame_field_flag;
    public int direct_8x8_inference_flag;
    public int frame_cropping_flag;
    public int frame_crop_left_offset;
    public int frame_crop_right_offset;
    public int frame_crop_top_offset;
    public int frame_crop_bottom_offset;
    public int vui_parameters_present_flag;
    public int aspect_ratio_info_present_flag;
    public int aspect_ratio_idc;
    public int sar_width;
    public int sar_height;
    public int overscan_info_present_flag;
    public int overscan_appropriate_flag;
    public int video_signal_type_present_flag;
    public int video_format;
    public int video_full_range_flag;
    public int colour_description_present_flag;
    public int colour_primaries;
    public int transfer_characteristics;
    public int matrix_coefficients;
    public int chroma_loc_info_present_flag;
    public int chroma_sample_loc_type_top_field;
    public int chroma_sample_loc_type_bottom_field;
    public int timing_info_present_flag;
    public int num_units_in_tick;
    public int time_scale;
    public int fixed_frame_rate_flag;
    public int nal_hrd_parameters_present_flag;
    public int vcl_hrd_parameters_present_flag;
    public int low_delay_hrd_flag;
    public int pic_struct_present_flag;
    public int bitstream_restriction_flag;
    public int motion_vectors_over_pic_boundaries_flag;
    public int max_bytes_per_pic_denom;
    public int max_bits_per_mb_denom;
    public int log2_max_mv_length_horizontal;
    public int log2_max_mv_length_vertical;
    public int num_reorder_frames;
    public int max_dec_frame_buffering;
    public int cpb_cnt_minus1;
    public int bit_rate_scale;
    public int cpb_size_scale;
    public int initial_cpb_removal_delay_length_minus1;
    public int cpb_removal_delay_length_minus1;
    public int dpb_output_delay_length_minus1;
    public int time_offset_length;
    private static final int[][] a;
    private int b;
    
    public TuSdkH264SPS(final byte[] data) {
        this.forbidden_zero_bit = 0;
        this.nal_ref_idc = 0;
        this.nal_unit_type = 0;
        this.profile_idc = 0;
        this.constraint_set0_flag = 0;
        this.constraint_set1_flag = 0;
        this.constraint_set2_flag = 0;
        this.constraint_set3_flag = 0;
        this.reserved_zero_4bits = 0;
        this.level_idc = 0;
        this.seq_parameter_set_id = 0;
        this.chroma_format_idc = 0;
        this.residual_colour_transform_flag = 0;
        this.bit_depth_luma_minus8 = 0;
        this.bit_depth_chroma_minus8 = 0;
        this.qpprime_y_zero_transform_bypass_flag = 0;
        this.seq_scaling_matrix_present_flag = 0;
        this.log2_max_frame_num_minus4 = 0;
        this.pic_order_cnt_type = 0;
        this.log2_max_pic_order_cnt_lsb_minus4 = 0;
        this.delta_pic_order_always_zero_flag = 0;
        this.offset_for_non_ref_pic = 0;
        this.offset_for_top_to_bottom_field = 0;
        this.num_ref_frames_in_pic_order_cnt_cycle = 0;
        this.num_ref_frames = 0;
        this.gaps_in_frame_num_value_allowed_flag = 0;
        this.pic_width_in_mbs_minus1 = 0;
        this.pic_height_in_map_units_minus1 = 0;
        this.frame_mbs_only_flag = 0;
        this.mb_adaptive_frame_field_flag = 0;
        this.direct_8x8_inference_flag = 0;
        this.frame_cropping_flag = 0;
        this.frame_crop_left_offset = 0;
        this.frame_crop_right_offset = 0;
        this.frame_crop_top_offset = 0;
        this.frame_crop_bottom_offset = 0;
        this.vui_parameters_present_flag = 0;
        this.aspect_ratio_info_present_flag = 0;
        this.aspect_ratio_idc = 0;
        this.sar_width = 0;
        this.sar_height = 0;
        this.overscan_info_present_flag = 0;
        this.overscan_appropriate_flag = 0;
        this.video_signal_type_present_flag = 0;
        this.video_format = 0;
        this.video_full_range_flag = 0;
        this.colour_description_present_flag = 0;
        this.colour_primaries = 0;
        this.transfer_characteristics = 0;
        this.matrix_coefficients = 0;
        this.chroma_loc_info_present_flag = 0;
        this.chroma_sample_loc_type_top_field = 0;
        this.chroma_sample_loc_type_bottom_field = 0;
        this.timing_info_present_flag = 0;
        this.num_units_in_tick = 0;
        this.time_scale = 0;
        this.fixed_frame_rate_flag = 0;
        this.nal_hrd_parameters_present_flag = 0;
        this.vcl_hrd_parameters_present_flag = 0;
        this.low_delay_hrd_flag = 0;
        this.pic_struct_present_flag = 0;
        this.bitstream_restriction_flag = 0;
        this.motion_vectors_over_pic_boundaries_flag = 0;
        this.max_bytes_per_pic_denom = 0;
        this.max_bits_per_mb_denom = 0;
        this.log2_max_mv_length_horizontal = 0;
        this.log2_max_mv_length_vertical = 0;
        this.num_reorder_frames = 0;
        this.max_dec_frame_buffering = 0;
        this.cpb_cnt_minus1 = 0;
        this.bit_rate_scale = 0;
        this.cpb_size_scale = 0;
        this.initial_cpb_removal_delay_length_minus1 = 0;
        this.cpb_removal_delay_length_minus1 = 0;
        this.dpb_output_delay_length_minus1 = 0;
        this.time_offset_length = 0;
        this.b = 32;
        this.data = data;
        if (data == null || data.length < 11 || data[0] != 0 || data[1] != 0 || data[2] != 0 || data[3] != 1) {
            TLog.w("%s null sps info.", "TuSdkH264SPS");
            return;
        }
        this.a();
    }
    
    private void a() {
        this.b(this.data);
        this.forbidden_zero_bit = this.a(this.data, 1);
        this.nal_ref_idc = this.a(this.data, 2);
        this.nal_unit_type = this.a(this.data, 5);
        if (this.nal_unit_type != 7) {
            return;
        }
        this.isSps = true;
        this.profile_idc = this.a(this.data, 8);
        this.constraint_set0_flag = this.a(this.data, 1);
        this.constraint_set1_flag = this.a(this.data, 1);
        this.constraint_set2_flag = this.a(this.data, 1);
        this.constraint_set3_flag = this.a(this.data, 1);
        this.reserved_zero_4bits = this.a(this.data, 4);
        this.level_idc = this.a(this.data, 8);
        this.seq_parameter_set_id = this.a(this.data);
        if (this.profile_idc == 100 || this.profile_idc == 110 || this.profile_idc == 122 || this.profile_idc == 144) {
            this.chroma_format_idc = this.a(this.data);
            if (this.chroma_format_idc == 3) {
                this.residual_colour_transform_flag = this.a(this.data, 1);
            }
            this.bit_depth_luma_minus8 = this.a(this.data);
            this.bit_depth_chroma_minus8 = this.a(this.data);
            this.qpprime_y_zero_transform_bypass_flag = this.a(this.data, 1);
            this.seq_scaling_matrix_present_flag = this.a(this.data, 1);
            this.seq_scaling_list_present_flag = new int[8];
            if (this.seq_scaling_matrix_present_flag != 0) {
                for (int i = 0; i < 8; ++i) {
                    this.seq_scaling_list_present_flag[i] = this.a(this.data, 1);
                }
            }
        }
        this.log2_max_frame_num_minus4 = this.a(this.data);
        this.pic_order_cnt_type = this.a(this.data);
        if (this.pic_order_cnt_type == 0) {
            this.log2_max_pic_order_cnt_lsb_minus4 = this.a(this.data);
        }
        else if (this.pic_order_cnt_type == 1) {
            this.delta_pic_order_always_zero_flag = this.a(this.data, 1);
            this.offset_for_non_ref_pic = this.c(this.data, -1);
            this.offset_for_top_to_bottom_field = this.c(this.data, -1);
            this.num_ref_frames_in_pic_order_cnt_cycle = this.a(this.data);
            final int[] array = new int[this.num_ref_frames_in_pic_order_cnt_cycle];
            for (int j = 0; j < this.num_ref_frames_in_pic_order_cnt_cycle; ++j) {
                array[j] = this.c(this.data, -1);
            }
        }
        this.num_ref_frames = this.a(this.data);
        this.gaps_in_frame_num_value_allowed_flag = this.a(this.data, 1);
        this.pic_width_in_mbs_minus1 = this.a(this.data);
        this.pic_height_in_map_units_minus1 = this.a(this.data);
        this.width = (this.pic_width_in_mbs_minus1 + 1) * 16;
        this.height = (this.pic_height_in_map_units_minus1 + 1) * 16;
        this.frame_mbs_only_flag = this.a(this.data, 1);
        if (this.frame_mbs_only_flag == 0) {
            this.mb_adaptive_frame_field_flag = this.a(this.data, 1);
        }
        this.direct_8x8_inference_flag = this.a(this.data, 1);
        this.frame_cropping_flag = this.a(this.data, 1);
        if (this.frame_cropping_flag != 0) {
            this.frame_crop_left_offset = this.a(this.data);
            this.frame_crop_right_offset = this.a(this.data);
            this.frame_crop_top_offset = this.a(this.data);
            this.frame_crop_bottom_offset = this.a(this.data);
        }
        this.vui_parameters_present_flag = this.a(this.data, 1);
        if (this.vui_parameters_present_flag != 0) {
            this.aspect_ratio_info_present_flag = this.a(this.data, 1);
            if (this.aspect_ratio_info_present_flag != 0) {
                this.aspect_ratio_idc = this.a(this.data, 8);
                if (this.aspect_ratio_idc == 255) {
                    this.sar_width = this.a(this.data, 16);
                    this.sar_height = this.a(this.data, 16);
                }
                else if (this.aspect_ratio_idc < TuSdkH264SPS.a.length) {
                    final int[] array2 = TuSdkH264SPS.a[this.aspect_ratio_idc];
                    this.sar_width = array2[0];
                    this.sar_height = array2[1];
                }
                if (this.sar_width > 0 && this.sar_height > 0 && this.width > 0 && this.height > 0) {
                    final int dar_width = this.width - this.frame_crop_left_offset * 2 - this.frame_crop_right_offset * 2;
                    final int dar_height = this.height - this.frame_crop_top_offset * 2 - this.frame_crop_bottom_offset * 2;
                    if (dar_width > dar_height) {
                        this.dar_width = dar_width;
                        this.dar_height = dar_height * this.sar_height / this.sar_width;
                    }
                    else {
                        this.dar_width = dar_width * this.sar_width / this.sar_height;
                        this.dar_height = dar_height;
                    }
                }
            }
            this.overscan_info_present_flag = this.a(this.data, 1);
            if (this.overscan_info_present_flag != 0) {
                this.overscan_appropriate_flag = this.a(this.data, 1);
            }
            this.video_signal_type_present_flag = this.a(this.data, 1);
            if (this.video_signal_type_present_flag != 0) {
                this.video_format = this.a(this.data, 3);
                this.video_full_range_flag = this.a(this.data, 1);
                this.colour_description_present_flag = this.a(this.data, 1);
                if (this.colour_description_present_flag != 0) {
                    this.colour_primaries = this.a(this.data, 8);
                    this.transfer_characteristics = this.a(this.data, 8);
                    this.matrix_coefficients = this.a(this.data, 8);
                }
            }
            this.chroma_loc_info_present_flag = this.a(this.data, 1);
            if (this.chroma_loc_info_present_flag != 0) {
                this.chroma_sample_loc_type_top_field = this.a(this.data);
                this.chroma_sample_loc_type_bottom_field = this.a(this.data);
            }
            this.timing_info_present_flag = this.a(this.data, 1);
            if (this.timing_info_present_flag != 0) {
                this.num_units_in_tick = this.a(this.data, 32);
                this.time_scale = this.a(this.data, 32);
                if (this.num_units_in_tick > 0 && this.time_scale > 0) {
                    this.fps = this.time_scale / this.num_units_in_tick;
                }
                this.fixed_frame_rate_flag = this.a(this.data, 1);
                if (this.fixed_frame_rate_flag != 0) {
                    this.fps /= 2;
                }
            }
        }
    }
    
    private int a(final byte[] array, final int n) {
        return this.a(array, n, -1);
    }
    
    private int a(final byte[] array, final int n, final int n2) {
        int n3 = 0;
        int b = (n2 == -1) ? this.b : n2;
        for (int i = 0; i < n; ++i) {
            n3 <<= 1;
            if ((array[b / 8] & 128 >> b % 8) != 0x0) {
                ++n3;
            }
            ++b;
        }
        this.b = b;
        return n3;
    }
    
    private int a(final byte[] array) {
        return this.b(array, -1);
    }
    
    private int b(final byte[] array, final int n) {
        int n2 = -1;
        int n3 = (n == -1) ? this.b : n;
        for (int i = 0; i != 1; i = this.a(array, 1, n3++), ++n2) {}
        final int n4 = (int)(Math.pow(2.0, n2) - 1.0 + this.a(array, n2, n3));
        this.b = n3 + n2;
        return n4;
    }
    
    private int c(final byte[] array, final int n) {
        final int b = this.b(array, n);
        return (int)(Math.pow(-1.0, b + 1) * Math.ceil(b / 2));
    }
    
    private int b(final byte[] array) {
        int length = array.length;
        for (int i = 0; i < length - 2; ++i) {
            if ((array[i] ^ 0x0) + (array[i + 1] ^ 0x0) + (array[i + 2] ^ 0x3) == 0) {
                for (int j = i + 2; j < length - 1; ++j) {
                    array[j] = array[j + 1];
                }
                --length;
            }
        }
        return length;
    }
    
    @Override
    public String toString() {
        final StringBuffer append = new StringBuffer("TuSdkH264SPS").append("{ \n");
        append.append("isSps: ").append(this.isSps).append(", \n");
        append.append("nal_unit_type: ").append(this.nal_unit_type).append(", \n");
        append.append("profile_idc: ").append(this.profile_idc).append(", \n");
        append.append("level_idc: ").append(this.level_idc).append(", \n");
        append.append("width: ").append(this.width).append(", \n");
        append.append("height: ").append(this.height).append(", \n");
        append.append("sar_width: ").append(this.sar_width).append(", \n");
        append.append("sar_height: ").append(this.sar_height).append(", \n");
        append.append("dar_width: ").append(this.dar_width).append(", \n");
        append.append("dar_height: ").append(this.dar_height).append(", \n");
        append.append("frame_crop_left_offset: ").append(this.frame_crop_left_offset).append(", \n");
        append.append("frame_crop_right_offset: ").append(this.frame_crop_right_offset).append(", \n");
        append.append("frame_crop_top_offset: ").append(this.frame_crop_top_offset).append(", \n");
        append.append("frame_crop_bottom_offset: ").append(this.frame_crop_bottom_offset).append(", \n");
        append.append("num_units_in_tick: ").append(this.num_units_in_tick).append(", \n");
        append.append("time_scale: ").append(this.time_scale).append(", \n");
        append.append("fps: ").append(this.fps).append(", \n");
        append.append("fixed_frame_rate_flag: ").append(this.fixed_frame_rate_flag).append(", \n");
        if (this.data != null) {
            append.append("sps: ").append(Arrays.toString(this.data)).append(", \n");
        }
        append.append("}");
        return append.toString();
    }
    
    static {
        a = new int[][] { { 0, 1 }, { 1, 1 }, { 12, 11 }, { 10, 11 }, { 16, 11 }, { 40, 33 }, { 24, 11 }, { 20, 11 }, { 32, 11 }, { 80, 33 }, { 18, 11 }, { 15, 11 }, { 64, 33 }, { 160, 99 }, { 4, 3 }, { 3, 2 }, { 2, 1 } };
    }
}
